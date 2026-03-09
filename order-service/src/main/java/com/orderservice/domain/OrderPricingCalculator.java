package com.orderservice.domain;

import com.commonlibs.api.http.product.ProductDto;
import com.orderservice.domain.db.OrderEntity;
import com.orderservice.domain.db.OrderItemEntity;
import com.orderservice.external.ProductHttpClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class OrderPricingCalculator implements PricingService {

    private final ProductHttpClient productClient;

    @Override
    public void calculatePrice(OrderEntity order) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemEntity item : order.getItems()) {
            ProductDto product = productClient.getProduct(item.getItemId());
            item.setPriceAtPurchase(product.price());
            item.setName(product.name());
            total = total.add(product.price().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setTotalAmount(total);
    }
}
