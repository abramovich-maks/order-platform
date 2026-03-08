package com.productservice.api.dto;

public record ReserveProductRequestDto(
        Long productId,
        Integer quantity
) {
}
