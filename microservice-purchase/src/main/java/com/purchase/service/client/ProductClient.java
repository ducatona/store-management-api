package com.purchase.service.client;

import com.purchase.exception.ApiError;
import com.purchase.exception.NegativeAmountException;
import com.purchase.exception.ResourceNotFoundException;
import com.purchase.model.dto.request.UpdateStockRequest;
import com.purchase.model.dto.response.ProductResponse;
import com.purchase.model.dto.response.ProductTypeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProductClient {


    private final WebClient webClient;



    public ProductClient(WebClient webClient) {
        this.webClient = webClient;
    }


    public ProductResponse getProductById(Long id) {

        return webClient.get().uri("http://localhost:8081/api/v1/product/{id}", id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> response.bodyToMono(ApiError.class)
                                .flatMap(apiError -> Mono.error(new ResourceNotFoundException(apiError.getMessage()))))
                .bodyToMono(ProductResponse.class)
                .block();

    }

    public void reduceStock(Long id, UpdateStockRequest request) {

        webClient.put()
                .uri("http://localhost:8081/api/v1/product/{id}/stock/", id)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> response.bodyToMono(ApiError.class)
                                .flatMap(apiError -> Mono.error(new ResourceNotFoundException(apiError.getMessage()))))
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(ApiError.class)
                                .flatMap(apiError -> Mono.error(new NegativeAmountException(apiError.getMessage()))))
                .bodyToMono(Void.class)
                .block();

    }


    public ProductTypeResponse getProductTypeByID(Long id) {

        return webClient
                .get()
                .uri("http://localhost:8081/api/v1/productType/{id}", id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> response.bodyToMono(ApiError.class)
                                .flatMap(apiError -> Mono.error(new ResourceNotFoundException(apiError.getMessage()))))
                .bodyToMono(ProductTypeResponse.class)
                .block();
    }


}
