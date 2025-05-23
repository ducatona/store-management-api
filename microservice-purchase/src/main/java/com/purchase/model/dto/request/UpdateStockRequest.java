package com.purchase.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateStockRequest {

    @Min(value = 0, message = " cannot be negative")
    @NotNull(message = " is required")
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
