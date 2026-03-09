package com.orderservice.api;

import com.commonlibs.api.http.payment.PaymentMethod;

public record OrderPaymentRequest(
        PaymentMethod paymentMethod
) {
}
