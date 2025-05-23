package com.product.controller.impl;

import com.product.controller.IProductController;
import com.product.exception.NegativeAmountException;
import com.product.exception.UnAuthorizedException;
import com.product.model.dto.request.ProductRequest;
import com.product.model.dto.request.UpdateStockRequest;
import com.product.model.dto.response.ProductResponse;
import com.product.exception.ResourceNotFoundException;
import com.product.model.dto.response.UpdateStockResponse;
import com.product.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/product")
public class ProductControllerImpl implements IProductController {

    private final ProductServiceImpl productService;

    public ProductControllerImpl(ProductServiceImpl productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProductResponse>> getProductById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequest));
    }

    @PutMapping("/{idProduct}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<ProductResponse>> updateProduct(@PathVariable Long idProduct, @Valid @RequestBody ProductRequest productRequest) throws ResourceNotFoundException, UnAuthorizedException {
        return ResponseEntity.ok(productService.updateProduct(idProduct,productRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(@PathVariable Long id) throws ResourceNotFoundException {
        productService.deleteProduct(id);
    }

    @PatchMapping("/stock/{idProduct}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UpdateStockResponse> updateStock(@PathVariable Long idProduct, @RequestBody UpdateStockRequest updateStockRequest) throws UnAuthorizedException, ResourceNotFoundException {
        return ResponseEntity.ok(productService.updateStock(idProduct, updateStockRequest));
    }


    @PutMapping("/{id}/stock/")
    public void reduceStock(@PathVariable Long id, @RequestBody UpdateStockRequest updateStockRequest) throws ResourceNotFoundException, NegativeAmountException {
        productService.reduceStock(id, updateStockRequest);
    }


}
