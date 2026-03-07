package com.orderservice.api;

import java.math.BigDecimal;

/**
 * DTO for {@link com.orderservice.domain.OrderItemEntity}
 */
public record OrderItemDto(Long id, Long itemId, Integer quantity, BigDecimal priceAtPurchase) {
}