package com.orderservice.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ProductHttpClientConfig {

    @Value("${product-service.base-url}")
    private String productServiceBaseUrl;

    @Bean
    RestClient productClient(RestClient.Builder builder) {
        return builder
                .baseUrl(productServiceBaseUrl)
                .build();
    }

    @Bean
    ProductHttpClient productHttpClient(RestClient restClient) {
        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(restClient))
                .build()
                .createClient(ProductHttpClient.class);
    }
}
