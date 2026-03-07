package com.orderservice.api;

import com.orderservice.domain.OrderItemEntity;

/**
 * DTO for {@link OrderItemEntity}
 */
public record OrderItemRequestDto(
        Long itemId,
        Integer quantity,
        String name) {
}