package com.productservice.api.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AllProductResponseDto(
        List<ProductDto> products
) {
}
