package com.productservice.api.dto;

import com.commonlibs.api.http.product.ProductDto;
import lombok.Builder;

import java.util.List;

@Builder
public record AllProductResponseDto(
        List<ProductDto> products
) {
}
