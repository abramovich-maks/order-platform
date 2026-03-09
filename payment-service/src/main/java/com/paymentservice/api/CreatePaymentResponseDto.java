package com.paymentservice.api;

import com.paymentservice.domain.PaymentEntity;
import com.paymentservice.domain.PaymentMethod;
import com.paymentservice.domain.PaymentStatus;

import java.math.BigDecimal;

/**
 * DTO for {@link PaymentEntity}
 */
public record CreatePaymentResponseDto(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod) {
}