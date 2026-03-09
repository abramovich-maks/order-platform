package com.productservice.domain;

import com.productservice.api.dto.AddProductRequestDto;
import com.commonlibs.api.http.product.AvailabilityProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductWarehouseTest {

    private final ProductEntityMapper productEntityMapper = Mappers.getMapper(ProductEntityMapper.class);
    private final ProductJpaRepository productRepository = new InMemoryProductRepository();
    private final ProductWarehouse productWarehouse = new ProductWarehouse(productRepository, productEntityMapper);

    @Test
    void should_add_product_and_save_to_database() {
        //given
        AddProductRequestDto request = new AddProductRequestDto("Banan", "yellow", BigDecimal.valueOf(10), 5);
        //when
        ProductEntity product = productWarehouse.addProduct(request);
        //then
        List<ProductEntity> allProducts = productWarehouse.getAll();
        assertThat(allProducts.size()).isEqualTo(1);
        assertThat(allProducts.get(0)).isEqualTo(product);
    }

    @Test
    void should_return_all_products() {
        // given
        AddProductRequestDto request1 = new AddProductRequestDto("Banan", "yellow", BigDecimal.TEN, 5);
        AddProductRequestDto request2 = new AddProductRequestDto("Apple", "red", BigDecimal.TEN, 5);
        productWarehouse.addProduct(request1);
        productWarehouse.addProduct(request2);
        // when
        List<ProductEntity> products = productWarehouse.getAll();
        // then
        assertThat(products).hasSize(2);
    }

    @Test
    void should_exception_when_product_not_found() {
        // given && when && then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> productWarehouse.getProductOrThrow(1L));
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void should_delete_product() {
        // given
        AddProductRequestDto request = new AddProductRequestDto("Banan", "yellow", BigDecimal.valueOf(10), 5);
        ProductEntity product = productWarehouse.addProduct(request);
        // when
        ProductEntity deleted = productWarehouse.delete(product.getId());
        // then
        assertThat(deleted).isEqualTo(product);
        List<ProductEntity> allProducts = productWarehouse.getAll();
        assertThat(allProducts.size()).isEqualTo(0);
    }

    @Test
    void should_reduce_stock() {
        // given
        AddProductRequestDto request = new AddProductRequestDto("Banan", "yellow", BigDecimal.valueOf(10), 5);
        ProductEntity product = productWarehouse.addProduct(request);
        // when
        ProductEntity reduced = productWarehouse.reduceStock(product.getId(), 2);
        // then
        assertThat(reduced.getQuantity()).isEqualTo(3);
        assertThat(reduced.getId()).isEqualTo(product.getId());
    }

    @Test
    void should_throw_exception_when_not_have_enough_stock() {
        // given
        AddProductRequestDto request = new AddProductRequestDto("Banan", "yellow", BigDecimal.valueOf(10), 5);
        ProductEntity product = productWarehouse.addProduct(request);
        // when && then
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> productWarehouse.reduceStock(product.getId(), 10));
        assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseStatusException.getReason()).isEqualTo("Not enough stock");
    }

    @Test
    void should_restock_product() {
        // given
        AddProductRequestDto request = new AddProductRequestDto("Banan", "yellow", BigDecimal.valueOf(10), 5);
        ProductEntity product = productWarehouse.addProduct(request);
        // when
        ProductEntity restocked = productWarehouse.restockProduct(product.getId(), 2);
        // then
        assertThat(restocked.getQuantity()).isEqualTo(7);
    }

    @Test
    void should_return_available_true_when_product_in_stock() {
        // given
        AddProductRequestDto request = new AddProductRequestDto("Banan", "yellow", BigDecimal.valueOf(10), 5);
        ProductEntity product = productWarehouse.addProduct(request);
        // when
        AvailabilityProductDto checked = productWarehouse.checkProductAvailability(1L);
        // then
        assertThat(checked.available()).isTrue();
    }

    @Test
    void should_return_available_false_when_product_out_of_stock() {
        // given
        AddProductRequestDto request = new AddProductRequestDto("Banan", "yellow", BigDecimal.valueOf(10), 0);
        ProductEntity product = productWarehouse.addProduct(request);
        // when
        AvailabilityProductDto checked = productWarehouse.checkProductAvailability(product.getId());
        // then
        assertThat(checked.available()).isFalse();
    }
}