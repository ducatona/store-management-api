package com.product.model.dto.response;

public class UpdateStockResponse {

    private Integer stock;

    public UpdateStockResponse() {
    }

    public UpdateStockResponse(Integer stock) {
        this.stock = stock;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
