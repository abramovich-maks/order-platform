package com.paymentservice.domain;

import com.paymentservice.api.CreatePaymentRequestDto;
import com.paymentservice.api.CreatePaymentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentEntityMapper {
    PaymentEntity toEntity(CreatePaymentRequestDto createPaymentRequestDto);

    CreatePaymentRequestDto toCreatePaymentRequestDto(PaymentEntity paymentEntity);

    PaymentEntity toEntity(CreatePaymentResponseDto createPaymentResponseDto);

    CreatePaymentResponseDto toCreatePaymentResponseDto(PaymentEntity paymentEntity);
}