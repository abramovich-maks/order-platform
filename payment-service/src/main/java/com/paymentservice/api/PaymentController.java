package com.paymentservice.api;

import com.commonlibs.api.http.payment.CreatePaymentRequestDto;
import com.commonlibs.api.http.payment.CreatePaymentResponseDto;
import com.paymentservice.domain.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public CreatePaymentResponseDto createPayment(@RequestBody CreatePaymentRequestDto request) {
        log.info("Creating payment request={}", request);
        return paymentService.createPayment(request);
    }
}