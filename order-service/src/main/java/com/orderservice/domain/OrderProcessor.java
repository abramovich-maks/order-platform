package com.orderservice.domain;

import com.commonlibs.api.http.order.CreateOrderRequestDto;
import com.commonlibs.api.http.order.OrderStatus;
import com.commonlibs.api.http.payment.CreatePaymentRequestDto;
import com.commonlibs.api.http.payment.PaymentStatus;
import com.commonlibs.api.http.product.ReserveProductRequestDto;
import com.commonlibs.api.http.product.RestockProductRequestDto;
import com.orderservice.api.OrderPaymentRequest;
import com.orderservice.domain.db.OrderEntity;
import com.orderservice.domain.db.OrderEntityMapper;
import com.orderservice.domain.db.OrderJpaRepository;
import com.orderservice.external.PaymentHttpClient;
import com.orderservice.external.ProductHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.commonlibs.api.http.order.OrderStatus.PENDING_PAYMENT;

@RequiredArgsConstructor
@Service
public class OrderProcessor {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final PricingService pricingService;
    private final ProductHttpClient productClient;
    private final PaymentHttpClient paymentClient;

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
        return orderJpaRepository.save(entity);
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
}
