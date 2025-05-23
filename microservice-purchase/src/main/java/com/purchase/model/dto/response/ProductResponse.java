package com.purchase.model.dto.response;

public class ProductResponse {

    private Long id;
    private String name;
    private double price;
    private Integer stock;
    private Long productTypeId;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, double price, Integer stock, Long productTypeId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.productTypeId = productTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
