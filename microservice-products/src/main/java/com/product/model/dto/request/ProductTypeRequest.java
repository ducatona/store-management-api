package com.product.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ProductTypeRequest {
    @NotBlank(message = "productType name is required")
    private String name;

    public ProductTypeRequest() {
    }


    public ProductTypeRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
