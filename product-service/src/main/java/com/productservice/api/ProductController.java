package com.productservice.api;

import com.productservice.api.dto.AddProductRequestDto;
import com.productservice.api.dto.AllProductResponseDto;
import com.productservice.api.dto.AvailabilityProductDto;
import com.productservice.api.dto.ProductDto;
import com.productservice.api.dto.ReserveProductRequestDto;
import com.productservice.api.dto.RestockProductRequestDto;
import com.productservice.domain.ProductEntity;
import com.productservice.domain.ProductEntityMapper;
import com.productservice.domain.ProductWarehouse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductWarehouse productWarehouse;
    private final ProductEntityMapper productEntityMapper;

    @PostMapping()
    public ResponseEntity<ProductDto> addProduct(@RequestBody final AddProductRequestDto request) {
        log.info("Adding product {}", request);
        var addProduct = productWarehouse.addProduct(request);
        ProductDto productDto = productEntityMapper.toProductDto(addProduct);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getOneProduct(@PathVariable final Long id) {
        log.info("Retrieving product with id: {}", id);
        var productEntity = productWarehouse.getProductOrThrow(id);
        ProductDto productDto = productEntityMapper.toProductDto(productEntity);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<AllProductResponseDto> getAll() {
        List<ProductEntity> all = productWarehouse.getAll();
        return ResponseEntity.ok(
                AllProductResponseDto.builder()
                        .products(productEntityMapper.toAllProductsDto(all))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductEntity> delete(@PathVariable final Long id) {
        return ResponseEntity.ok(productWarehouse.delete(id));
    }

    @PostMapping("/reserve")
    public ResponseEntity<ProductDto> reserve(@RequestBody ReserveProductRequestDto request) {
        ProductEntity product = productWarehouse.reduceStock(request.productId(), request.quantity());
        return ResponseEntity.ok(productEntityMapper.toProductDto(product));
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<ProductDto> restock(@PathVariable Long id, @RequestBody RestockProductRequestDto request) {
        ProductEntity product = productWarehouse.restockProduct(id, request.quantity());
        return ResponseEntity.ok(productEntityMapper.toProductDto(product));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<AvailabilityProductDto> checkProductAvailability(@PathVariable final Long id) {
        log.info("Checking product availability with id: {}", id);
        AvailabilityProductDto availabilityDto = productWarehouse.checkProductAvailability(id);
        return ResponseEntity.ok().body(availabilityDto);
    }
}
