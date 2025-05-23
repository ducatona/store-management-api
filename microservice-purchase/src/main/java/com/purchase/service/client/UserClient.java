package com.purchase.service.client;

import com.purchase.exception.ApiError;
import com.purchase.exception.ResourceNotFoundException;
import com.purchase.model.dto.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserClient {



    private final WebClient webClient;

    public UserClient(WebClient webClient){
        this.webClient = webClient;
    }

    public UserResponse getUserById(Long id){

        return webClient.get()
                .uri("http://localhost:8082/api/v1/user/{id}", id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> response.bodyToMono(ApiError.class)
                                .flatMap(apiError -> Mono.error(new ResourceNotFoundException(apiError.getMessage()))))
                .bodyToMono(UserResponse.class)
                .block();

    }






}
