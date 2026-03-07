package com.orderservice.domain;

import com.orderservice.api.CreateOrderRequestDto;
import com.orderservice.api.OrderDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderEntityMapper {
    OrderEntity toEntity(OrderDto orderDto);

    @AfterMapping
    default void linkOrderItemEntities(@MappingTarget OrderEntity orderEntity) {
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
    }

    OrderDto toOrderDto(OrderEntity orderEntity);

    OrderEntity toEntity(CreateOrderRequestDto createOrderRequestDto);

    CreateOrderRequestDto toCreateOrderRequestDto(OrderEntity orderEntity);
}