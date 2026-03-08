package com.productservice.domain;

import com.productservice.api.dto.AddProductRequestDto;
import com.productservice.api.dto.AvailabilityProductDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductWarehouse {

    private final ProductJpaRepository productJpaRepository;
    private final ProductEntityMapper productEntityMapper;

    public ProductEntity addProduct(final AddProductRequestDto request) {
        var entity = productEntityMapper.toEntity(request);
        return productJpaRepository.save(entity);
    }

    public ProductEntity getProductOrThrow(final Long id) {
        var productEntityOptional = productJpaRepository.findById(id);
        return productEntityOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public List<ProductEntity> getAll() {
        return productJpaRepository.findAll();
    }

    public ProductEntity delete(final Long id) {
        ProductEntity product = getProductOrThrow(id);
        productJpaRepository.delete(product);
        return product;
    }

    @Transactional
    public ProductEntity reduceStock(Long productId, Integer quantity) {
        ProductEntity product = getProductOrThrow(productId);
        if (product.getQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock");
        }
        product.setQuantity(product.getQuantity() - quantity);
        return product;
    }

    @Transactional
    public ProductEntity restockProduct(Long productId, Integer quantity) {
        ProductEntity product = getProductOrThrow(productId);
        product.setQuantity(product.getQuantity() + quantity);
        return product;
    }

    public AvailabilityProductDto checkProductAvailability(final Long id) {
        ProductEntity product = getProductOrThrow(id);
        boolean available = product.getQuantity() > 0;
        return AvailabilityProductDto.builder()
                .id(product.getId())
                .available(available)
                .quantity(product.getQuantity())
                .build();
    }
}
