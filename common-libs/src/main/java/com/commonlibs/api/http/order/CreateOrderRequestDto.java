package com.commonlibs.api.http.order;

import java.util.Set;


public record CreateOrderRequestDto(
        Long customersId,
        String address,
        Set<OrderItemRequestDto> items
) {
}