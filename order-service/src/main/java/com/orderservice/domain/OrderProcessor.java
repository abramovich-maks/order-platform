package com.orderservice.domain;

import com.orderservice.api.CreateOrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class OrderProcessor {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;

    public OrderEntity create(final CreateOrderRequestDto request) {
        var entity = orderEntityMapper.toEntity(request);
        getCalculatePricingForOrder(entity);
        entity.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        return orderJpaRepository.save(entity);
    }

    public OrderEntity getOrderOrThrow(final Long id) {
        var orderItemEntityOptional = orderJpaRepository.findById(id);
        return orderItemEntityOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    private void getCalculatePricingForOrder(final OrderEntity entity) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemEntity item : entity.getItems()) {
            var randomPrice = ThreadLocalRandom.current().nextDouble(10, 100);
            item.setPriceAtPurchase(new BigDecimal(randomPrice));
            totalAmount = item.getPriceAtPurchase()
                    .multiply(BigDecimal.valueOf(item.getQuantity())
                            .add(totalAmount));
        }
        entity.setTotalAmount(totalAmount);
    }
}
