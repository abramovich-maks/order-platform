package com.orderservice.api;

import com.orderservice.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO for {@link com.orderservice.domain.OrderEntity}
 */
public record OrderDto(
        Long id,
        Long customersId,
        String address,
        BigDecimal totalAmount,
        String courierName,
        Integer etaMinutes,
        OrderStatus orderStatus,
        Set<OrderItemDto> items
) {
}