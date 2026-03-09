package com.orderservice;

import com.commonlibs.api.http.order.CreateOrderRequestDto;
import com.commonlibs.api.http.order.OrderStatus;
import com.orderservice.domain.OrderPricingCalculator;
import com.orderservice.domain.OrderProcessor;
import com.orderservice.domain.PricingService;
import com.orderservice.domain.db.OrderEntity;
import com.orderservice.domain.db.OrderEntityMapper;
import com.orderservice.domain.db.OrderJpaRepository;
import com.orderservice.external.PaymentHttpClient;
import com.orderservice.external.ProductHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderServiceApplicationTests {

    private final OrderJpaRepository orderJpaRepository = new InMemoryOrderRepository();
    private final OrderEntityMapper orderEntityMapper = mock(OrderEntityMapper.class);
    private final ProductHttpClient productClient = mock(ProductHttpClient.class);
    private final PaymentHttpClient paymentClient = mock(PaymentHttpClient.class);
    private final PricingService pricingService = new OrderPricingCalculator(productClient);
    private final OrderProcessor orderProcessor = new OrderProcessor(orderJpaRepository, orderEntityMapper, pricingService, productClient, paymentClient);


    @Test
    public void should_retrieve_order_by_id() {
        // given
        OrderEntity orderEntity = new OrderEntity();
        OrderEntity saved = orderJpaRepository.save(orderEntity);
        // when
        OrderEntity result = orderProcessor.getOrderOrThrow(saved.getId());
        // then
        Assertions.assertEquals(saved.getId(), result.getId());
    }

    @Test
    void should_exception_when_order_not_found() {
        // given && when && then
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> orderProcessor.getOrderOrThrow(1L));
        Assertions.assertEquals("404 NOT_FOUND \"Entity with id `1` not found\"", responseStatusException.getMessage());
    }

    @Test
    void should_create_order() {
        // given
        CreateOrderRequestDto request = new CreateOrderRequestDto(1L, "Street 123", Set.of());
        OrderEntity entity = new OrderEntity();
        entity.setItems(new HashSet<>());

        when(orderEntityMapper.toEntity(request)).thenReturn(entity);
        // when
        OrderEntity result = orderProcessor.create(request);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.PENDING_PAYMENT);
    }
}
