package com.product.model.dto.request;


import jakarta.validation.constraints.*;

public class ProductRequest {


    @NotBlank(message = "product name is required")
    private String name;

    @NotNull(message = "product price is required")
    @DecimalMin(value = "0.1", message = "The price cannot be negative")
    private double price;

    @Min(value = 0, message = "the stock cannot be negative")
    @NotNull(message = "product stock is required")
    private int stock;

    @NotNull(message = "product type is required")
    private Long productTypeId;

    public ProductRequest(String name, double price, int stock, Long productTypeId) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.productTypeId = productTypeId;
    }

    public ProductRequest() {
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }
}
