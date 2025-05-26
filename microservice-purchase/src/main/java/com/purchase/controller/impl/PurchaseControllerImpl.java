package com.purchase.controller.impl;

import com.purchase.controller.IPurchaseController;
import com.purchase.exception.ResourceNotFoundException;
import com.purchase.model.dto.request.OrderRequest;
import com.purchase.model.dto.response.OrderResponse;
import com.purchase.service.impl.PurchaseServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/purchase")
public class PurchaseControllerImpl implements IPurchaseController {

    private final PurchaseServiceImpl service;

    public PurchaseControllerImpl(PurchaseServiceImpl service) {
        this.service = service;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllPurchase() {
        return ResponseEntity.ok(service.getAllPurchases());
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<OrderResponse>> getUserPurchase(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllPurchasesByUser(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> getPurchaseById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(service.getPurchaseById(id));
    }

    @GetMapping("productType/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getProductTypePurchase(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllPurchasesByProductType(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponse> createPurchase(@Valid @RequestBody OrderRequest orderRequest) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createPurchase(orderRequest));
    }




}

