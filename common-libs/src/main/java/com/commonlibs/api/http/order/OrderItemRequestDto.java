package com.commonlibs.api.http.order;

public record OrderItemRequestDto(
        Long itemId,
        Integer quantity) {
}