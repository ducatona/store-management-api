package com.purchase.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class OrderRequest {

    @NotNull(message = " is required")
    private Long userId;
    @NotNull(message = " is required")
    private Long productId;
    @NotNull(message = " is required")
    private Long productTypeId;
    @Min(value = 0, message = " cannot be negative")
    @NotNull(message = " is required")
    private Integer quantity;

    public OrderRequest() {
    }

    public OrderRequest(Long userId, Long productId, Long productTypeId, Integer quantity) {
        this.userId = userId;
        this.productId = productId;
        this.productTypeId = productTypeId;
        this.quantity = quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
