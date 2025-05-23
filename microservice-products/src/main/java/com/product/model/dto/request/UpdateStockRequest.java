package com.product.model.dto.request;

public class UpdateStockRequest {

    private Integer newStock;


    public UpdateStockRequest() {
    }


    public UpdateStockRequest(Integer newStock) {
        this.newStock = newStock;
    }


    public Integer getNewStock() {
        return newStock;
    }

    public void setNewStock(Integer newStock) {
        this.newStock = newStock;
    }



}
