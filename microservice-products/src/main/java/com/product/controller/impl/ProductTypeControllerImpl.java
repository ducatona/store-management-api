package com.product.controller.impl;

import com.product.controller.IProductTypeController;
import com.product.model.dto.request.ProductTypeRequest;
import com.product.model.dto.response.ProductTypeResponse;
import com.product.exception.ResourceNotFoundException;
import com.product.service.impl.ProductTypeServiceImpl;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/productType")
public class ProductTypeControllerImpl implements IProductTypeController {

    private final ProductTypeServiceImpl productTypeServiceIml;

    public ProductTypeControllerImpl(ProductTypeServiceImpl productServiceTypeIml) {
        this.productTypeServiceIml = productServiceTypeIml;
    }

    @GetMapping
    public ResponseEntity<List<ProductTypeResponse>> getAllProducts() {
        return ResponseEntity.ok(productTypeServiceIml.getAllProductsTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProductTypeResponse>> getProductById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(productTypeServiceIml.getProductTypeById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductTypeResponse> createProductType(@Valid @RequestBody ProductTypeRequest productTypeRequest) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(productTypeServiceIml.createProductType(productTypeRequest));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<ProductTypeResponse>> updateProductType(@PathVariable Long id, @Valid @RequestBody ProductTypeRequest productTypeRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(productTypeServiceIml.updateProductType(id, productTypeRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProductType(@PathVariable Long id) throws ResourceNotFoundException {
        productTypeServiceIml.deleteProductType(id);
    }

}
