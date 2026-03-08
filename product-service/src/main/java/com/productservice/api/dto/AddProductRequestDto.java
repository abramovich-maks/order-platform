package com.productservice.api.dto;

import com.productservice.domain.ProductEntity;

import java.math.BigDecimal;

/**
 * DTO for {@link ProductEntity}
 */
public record AddProductRequestDto(
        String name,
        String description,
        BigDecimal price,
        Integer quantity
) {
}