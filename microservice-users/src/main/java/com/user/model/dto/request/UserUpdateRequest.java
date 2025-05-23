package com.user.model.dto.request;

import com.user.validation.EmailRegistered;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequest {


    @NotBlank(message = "Cannot be null or empty")
    private String name;
    @EmailRegistered
    @Email
    private String email;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
