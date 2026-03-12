package com.orderservice.domain;

import com.commonlibs.api.http.order.CreateOrderRequestDto;
import com.commonlibs.api.http.order.OrderStatus;
import com.commonlibs.api.http.payment.CreatePaymentRequestDto;
import com.commonlibs.api.http.payment.CreatePaymentResponseDto;
import com.commonlibs.api.http.payment.PaymentStatus;
import com.commonlibs.api.http.product.ReserveProductRequestDto;
import com.commonlibs.api.http.product.RestockProductRequestDto;
import com.commonlibs.api.kafka.DeliveryAssignedEvent;
import com.commonlibs.api.kafka.OrderPaidEvent;
import com.orderservice.api.OrderPaymentRequest;
import com.orderservice.domain.db.OrderEntity;
import com.orderservice.domain.db.OrderEntityMapper;
import com.orderservice.domain.db.OrderJpaRepository;
import com.orderservice.external.PaymentHttpClient;
import com.orderservice.external.ProductHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.commonlibs.api.http.order.OrderStatus.PENDING_PAYMENT;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderProcessor {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final PricingService pricingService;
    private final ProductHttpClient productClient;
    private final PaymentHttpClient paymentClient;
    private final KafkaTemplate<Long, OrderPaidEvent> kafkaTemplate;

    @Value("${order-paid-topic}")
    private String orderPaidTopic;

    public OrderEntity create(final CreateOrderRequestDto request) {
        var entity = orderEntityMapper.toEntity(request);
        pricingService.calculatePrice(entity);
        entity.setOrderStatus(PENDING_PAYMENT);
        entity = orderJpaRepository.save(entity);
        reserveProducts(entity);
        return entity;
    }

    public OrderEntity getOrderOrThrow(final Long id) {
        var orderItemEntityOptional = orderJpaRepository.findById(id);
        return orderItemEntityOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public OrderEntity processPayment(Long id, OrderPaymentRequest request) {
        var entity = getOrderOrThrow(id);
        if (entity.getOrderStatus() != PENDING_PAYMENT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is not in PENDING_PAYMENT state");
        }
        var response = paymentClient.createPayment(CreatePaymentRequestDto.builder()
                .orderId(id)
                .amount(entity.getTotalAmount())
                .paymentMethod(request.paymentMethod()).build());

        OrderStatus status = response.paymentStatus() == PaymentStatus.PAYMENT_SUCCESSFUL
                ? OrderStatus.PAID
                : OrderStatus.PAYMENT_FAILED;
        entity.setOrderStatus(status);

        if (status == OrderStatus.PAYMENT_FAILED) {
            restockProducts(entity);
        }
        sendOrderEvent(entity, response);
        return orderJpaRepository.save(entity);
    }

    private void sendOrderEvent(final OrderEntity entity, CreatePaymentResponseDto paymentResponseDto) {
        kafkaTemplate.send(
                orderPaidTopic,
                entity.getId(),
                OrderPaidEvent.builder()
                        .orderId(entity.getId())
                        .paymentId(paymentResponseDto.id())
                        .amount(entity.getTotalAmount())
                        .paymentMethod(paymentResponseDto.paymentMethod()).build()
        ).thenAccept(result -> {
                    log.info("Order Paid event sent: id={}", entity.getId());
                }
        );
    }

    private void restockProducts(final OrderEntity entity) {
        for (var item : entity.getItems()) {
            productClient.restockProduct(
                    item.getItemId(),
                    new RestockProductRequestDto(
                            item.getQuantity()
                    )
            );
        }
    }

    private void reserveProducts(final OrderEntity entity) {
        for (var item : entity.getItems()) {
            productClient.reserveProduct(
                    new ReserveProductRequestDto(
                            item.getItemId(),
                            item.getQuantity()
                    )
            );
        }
    }

    public void processDeliveryAssigned(DeliveryAssignedEvent event) {
        var order = getOrderOrThrow(event.orderId());
        if (!order.getOrderStatus().equals(OrderStatus.PAID)) {
            processIncorrectDeliveryState(order);
            return;
        }

        order.setOrderStatus(OrderStatus.DELIVERY_ASSIGNED);
        order.setCourierName(event.courierName());
        order.setEtaMinutes(event.etaMinutes());
        orderJpaRepository.save(order);
        log.info("Order delivery assigned processed: orderId={}", order.getId());
    }

    private void processIncorrectDeliveryState(OrderEntity order) {
        if (order.getOrderStatus().equals(OrderStatus.DELIVERY_ASSIGNED)) {
            log.info("Order delivery already processed: orderId={}", order.getId());
        } else {
            log.error("Trying to assign delivery but order have incorrect state: state={}", order.getId());
        }
    }
}
