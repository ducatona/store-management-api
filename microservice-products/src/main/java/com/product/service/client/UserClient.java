package com.product.service.client;

import com.product.model.dto.response.UserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class UserClient {

private final WebClient webClient;
public UserClient(WebClient webClient) {
    this.webClient = webClient;
}

public String getUserById(Long idUser) {

    return webClient.get()
            .uri("http://localhost:8082/api/v1/user/{id}", idUser)
            .retrieve()
            .bodyToMono(UserResponse.class)
            .map(UserResponse::getRole)
            .block();

}





}
