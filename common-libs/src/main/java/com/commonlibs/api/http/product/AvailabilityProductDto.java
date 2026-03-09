package com.commonlibs.api.http.product;

import lombok.Builder;

@Builder
public record AvailabilityProductDto(
        Long id,
        Boolean available,
        Integer quantity
) {
}
