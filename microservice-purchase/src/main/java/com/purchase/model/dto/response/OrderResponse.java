package com.purchase.model.dto.response;

public class OrderResponse {

    private Long orderId;
    private Long userId;
    private Long productId;
    private int quantity;
    private Long productTypeId;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId, Long userId, Long productId, int quantity, Double totalPrice, String status, Long productTypeId) {

        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.productTypeId = productTypeId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }
}
