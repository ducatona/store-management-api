package com.purchase.model.dto.request;

public class OrderUpdateRequest {

    private Integer newQuantity;

    public OrderUpdateRequest() {
    }

    public OrderUpdateRequest(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }

    public Integer getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }


}
