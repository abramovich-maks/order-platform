package com.commonlibs.api.http.payment;


import java.math.BigDecimal;

public record CreatePaymentRequestDto(
        Long orderId,
        BigDecimal amount,
        PaymentMethod paymentMethod
) {
}