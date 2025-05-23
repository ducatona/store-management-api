package com.purchase.service.client;


import com.purchase.model.dto.response.ValidateResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthClient {


    private final WebClient webClient;
    public AuthClient(WebClient webClient) {
        this.webClient = webClient;
    }


    public ValidateResponse validateToken(String token) {

        return webClient.get()
                .uri("http://localhost:8084/api/auth/validate")
                .header("Authorization",  token)
                .retrieve()
                .bodyToMono(ValidateResponse.class)
                .block();
    }


}
