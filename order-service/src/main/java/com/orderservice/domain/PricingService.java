package com.orderservice.domain;

import com.orderservice.domain.db.OrderEntity;

public interface PricingService {
    void calculatePrice(OrderEntity order);
}
