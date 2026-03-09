package com.commonlibs.api.http.order;


import java.math.BigDecimal;
import java.util.Set;

public record OrderDto(
        Long id,
        Long customersId,
        String address,
        BigDecimal totalAmount,
        String courierName,
        Integer etaMinutes,
        OrderStatus orderStatus,
        Set<OrderItemDto> items
) {
}