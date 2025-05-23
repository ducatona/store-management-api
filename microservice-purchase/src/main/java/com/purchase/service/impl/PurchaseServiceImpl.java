package com.purchase.service.impl;

import com.purchase.exception.ResourceNotFoundException;
import com.purchase.model.dto.request.UpdateStockRequest;
import com.purchase.repository.Order;
import com.purchase.model.dto.request.OrderRequest;
import com.purchase.model.dto.response.OrderResponse;
import com.purchase.model.dto.response.ProductResponse;
import com.purchase.model.dto.response.ProductTypeResponse;
import com.purchase.model.dto.response.UserResponse;
import com.purchase.repository.IOrderRepository;
import com.purchase.service.IPurchaseService;
import com.purchase.service.client.ProductClient;
import com.purchase.service.client.UserClient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements IPurchaseService {

    private final ProductClient productClient;
    private final UserClient userClient;
    private final ModelMapper modelMapper;
    private final IOrderRepository repository;

    public PurchaseServiceImpl(ProductClient productClient, UserClient userClient, ModelMapper modelMapper, IOrderRepository repository) {
        this.productClient = productClient;
        this.userClient = userClient;
        this.modelMapper = modelMapper;
        this.repository = repository;
    }


    public List<OrderResponse> getAllPurchasesByProductType(Long productTypeId) {
        return repository.findByProductTypeId(productTypeId).stream()
                .map(order -> modelMapper.map(order, OrderResponse.class)).collect(Collectors.toList());
    }


    public OrderResponse getPurchaseById(Long id) throws ResourceNotFoundException {
        Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not order found with id: " + id));
        return modelMapper.map(order, OrderResponse.class);
    }


    public List<OrderResponse> getAllPurchasesByUser(Long userId) {
        return repository.findByUserId(userId).stream().map(order -> modelMapper.map(order, OrderResponse.class)).collect(Collectors.toList());
    }

    public List<OrderResponse> getAllPurchases() {
        return repository.findAll().stream().map(order -> modelMapper.map(order, OrderResponse.class)).collect(Collectors.toList());
    }

    public OrderResponse createPurchase(OrderRequest request) throws ResourceNotFoundException {

        ProductResponse productResponse = productClient.getProductById(request.getProductId());
        ProductTypeResponse productTypeResponse = productClient.getProductTypeByID(request.getProductTypeId());
        UserResponse userResponse = userClient.getUserById(request.getUserId());

        if(!productResponse.getProductTypeId().equals(productTypeResponse.getId())){
            throw new ResourceNotFoundException("No product found with that product type");
        }

        Order order = new Order();
        order.setProductId(productResponse.getId());
        order.setProductTypeId(productTypeResponse.getId());
        order.setUserId(userResponse.getId());
        order.setQuantity(request.getQuantity());


        UpdateStockRequest stockUpdateRequest = new UpdateStockRequest(request.getQuantity());

        productClient.reduceStock(order.getProductId(), stockUpdateRequest);

        repository.save(order);

        return modelMapper.map(order, OrderResponse.class);

    }

}




