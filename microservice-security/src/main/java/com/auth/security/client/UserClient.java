package com.auth.security.client;


import com.auth.security.exception.ApiError;
import com.auth.security.exception.ResourceNotFoundException;
import com.auth.security.model.response.UserResponseAuth;
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


    public UserResponseAuth getUserByEmail(String email){

        return webClient.get()
                .uri("http://localhost:8082/api/v1/user/email/{email}", email)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> response.bodyToMono(ApiError.class)
                                .flatMap(apiError -> Mono.error(new ResourceNotFoundException(apiError.getMessage()))))
                .bodyToMono(UserResponseAuth.class)
                .block();

    }






}
