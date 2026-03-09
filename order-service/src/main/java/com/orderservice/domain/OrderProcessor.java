package com.orderservice.domain;

import com.commonlibs.api.http.order.CreateOrderRequestDto;
import com.commonlibs.api.http.product.ReserveProductRequestDto;
import com.orderservice.domain.db.OrderEntity;
import com.orderservice.domain.db.OrderEntityMapper;
import com.orderservice.domain.db.OrderJpaRepository;
import com.orderservice.external.ProductHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.commonlibs.api.http.order.OrderStatus.PENDING_PAYMENT;

@RequiredArgsConstructor
@Service
public class OrderProcessor {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final PricingService pricingService;
    private final ProductHttpClient productClient;

    public OrderEntity create(final CreateOrderRequestDto request) {
        var entity = orderEntityMapper.toEntity(request);
        pricingService.calculatePrice(entity);
        for (var item : entity.getItems()) {
            productClient.reserveProduct(
                    new ReserveProductRequestDto(
                            item.getItemId(),
                            item.getQuantity()
                    )
            );
        }
        entity.setOrderStatus(PENDING_PAYMENT);
        return orderJpaRepository.save(entity);
    }

    public OrderEntity getOrderOrThrow(final Long id) {
        var orderItemEntityOptional = orderJpaRepository.findById(id);
        return orderItemEntityOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }
}
