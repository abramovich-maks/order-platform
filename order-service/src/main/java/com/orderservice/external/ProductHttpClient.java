package com.orderservice.external;


import com.commonlibs.api.http.product.AvailabilityProductDto;
import com.commonlibs.api.http.product.ProductDto;
import com.commonlibs.api.http.product.ReserveProductRequestDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(
        url = "api/products",
        accept = "application/json",
        contentType = "application/json"
)
public interface ProductHttpClient {

    @GetExchange("/{id}")
    ProductDto getProduct(@PathVariable Long id);

    @PostExchange("/reserve")
    ProductDto reserveProduct(@RequestBody ReserveProductRequestDto request);

}
