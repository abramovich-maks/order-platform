package com.paymentservice.domain;

import com.commonlibs.api.http.payment.CreatePaymentRequestDto;
import com.commonlibs.api.http.payment.CreatePaymentResponseDto;
import com.commonlibs.api.http.payment.PaymentMethod;
import com.commonlibs.api.http.payment.PaymentStatus;
import com.paymentservice.domain.db.PaymentEntity;
import com.paymentservice.domain.db.PaymentEntityMapper;
import com.paymentservice.domain.db.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentEntityMapper paymentEntityMapper;

    public CreatePaymentResponseDto createPayment(CreatePaymentRequestDto request) {

        var found = paymentJpaRepository.findByOrderId(request.orderId());

        if (found.isPresent()) {
            log.info("Payment already exists for order id: {}", request.orderId());
            return paymentEntityMapper.toCreatePaymentResponseDto(found.get());
        }

        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(request.orderId());
        entity.setAmount(request.amount());
        entity.setPaymentMethod(request.paymentMethod());

        boolean success = processPayment(request.paymentMethod());
        if (success) {
            entity.setPaymentStatus(PaymentStatus.PAYMENT_SUCCESSFUL);
        } else {
            entity.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
        }

        PaymentEntity saved = paymentJpaRepository.save(entity);
        return paymentEntityMapper.toCreatePaymentResponseDto(saved);
    }


    private boolean processPayment(PaymentMethod method) {
        int chance = ThreadLocalRandom.current().nextInt(100);

        return switch (method) {
            case CREDIT_CARD -> chance < 90;
            case APPLE_PAY -> chance < 98;
            case GOOGLE_PAY -> chance < 10;
            case CASH -> true;
        };
    }
}
