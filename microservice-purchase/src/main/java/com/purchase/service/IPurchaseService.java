package com.purchase.service;

import com.purchase.exception.ResourceNotFoundException;
import com.purchase.model.dto.request.OrderRequest;
import com.purchase.model.dto.response.OrderResponse;

import java.util.List;

public interface IPurchaseService {

    List<OrderResponse> getAllPurchasesByUser(Long userId);
    List<OrderResponse> getAllPurchases();
    OrderResponse createPurchase(OrderRequest request) throws ResourceNotFoundException;
    List<OrderResponse> getAllPurchasesByProductType(Long productTypeId);
    OrderResponse getPurchaseById(Long id) throws ResourceNotFoundException;

}
