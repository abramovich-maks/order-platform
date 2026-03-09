package com.orderservice.external;

import com.commonlibs.api.http.payment.CreatePaymentRequestDto;
import com.commonlibs.api.http.payment.CreatePaymentResponseDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(
        url = "api/payments",
        contentType = "application/json",
        accept = "application/json"
)
public interface PaymentHttpClient {

    @PostExchange
    CreatePaymentResponseDto createPayment(@RequestBody CreatePaymentRequestDto request);
}
