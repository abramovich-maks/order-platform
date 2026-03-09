package com.commonlibs.api.http.payment;


import java.math.BigDecimal;

public record CreatePaymentResponseDto(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod) {
}