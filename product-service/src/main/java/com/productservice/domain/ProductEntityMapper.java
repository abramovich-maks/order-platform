package com.productservice.domain;

import com.productservice.api.dto.AddProductRequestDto;
import com.productservice.api.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ProductEntityMapper {
    ProductEntity toEntity(ProductDto productDto);

    ProductDto toProductDto(ProductEntity productEntity);

    ProductEntity toEntity(AddProductRequestDto addProductRequestDto);

    AddProductRequestDto toAddProductRequestDto(ProductEntity productEntity);

    List<ProductDto> toAllProductsDto(List<ProductEntity> productEntities);
}