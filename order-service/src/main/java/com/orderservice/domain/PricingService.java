package com.orderservice.domain;

public interface PricingService {
    void calculatePrice(OrderEntity order);
}
