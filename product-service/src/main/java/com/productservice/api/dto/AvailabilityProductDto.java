package com.productservice.api.dto;

import lombok.Builder;

@Builder
public record AvailabilityProductDto(
        Long id,
        Boolean available,
        Integer quantity
) {
}
