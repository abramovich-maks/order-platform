package com.paymentservice.api;

import com.paymentservice.domain.PaymentEntity;
import com.paymentservice.domain.PaymentMethod;

import java.math.BigDecimal;

/**
 * DTO for {@link PaymentEntity}
 */
public record CreatePaymentRequestDto(
        Long orderId,
        BigDecimal amount,
        PaymentMethod paymentMethod
) {
}