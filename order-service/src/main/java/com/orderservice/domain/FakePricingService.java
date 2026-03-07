package com.orderservice.domain;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class FakePricingService implements PricingService {

    @Override
    public void calculatePrice(OrderEntity order) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemEntity item : order.getItems()) {
            BigDecimal price = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10, 100));
            item.setPriceAtPurchase(price);
            total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setTotalAmount(total);
    }
}
