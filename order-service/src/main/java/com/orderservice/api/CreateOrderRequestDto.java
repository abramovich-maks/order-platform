package com.orderservice.api;

import com.orderservice.domain.OrderEntity;

import java.util.Set;

/**
 * DTO for {@link OrderEntity}
 */
public record CreateOrderRequestDto(
        Long customersId,
        String address,
        Set<OrderItemRequestDto> items
) {
}