package com.commonlibs.api.http.product;

public record ReserveProductRequestDto(
        Long productId,
        Integer quantity
) {
}
