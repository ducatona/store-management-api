package com.product.service;


import com.product.exception.NegativeAmountException;
import com.product.exception.UnAuthorizedException;
import com.product.model.dto.request.ProductRequest;
import com.product.model.dto.request.UpdateStockRequest;
import com.product.model.dto.response.ProductResponse;
import com.product.exception.ResourceNotFoundException;
import com.product.model.dto.response.UpdateStockResponse;


import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<ProductResponse> getAllProducts();
    Optional<ProductResponse>getProductById(Long id) throws ResourceNotFoundException;
    ProductResponse createProduct(ProductRequest productRequest) throws ResourceNotFoundException;
    Optional<ProductResponse> updateProduct(Long idProduct,ProductRequest productRequest) throws ResourceNotFoundException, UnAuthorizedException;
    void deleteProduct(Long id) throws ResourceNotFoundException;
    void reduceStock(Long id, UpdateStockRequest updateStockRequest)throws ResourceNotFoundException, NegativeAmountException;
    UpdateStockResponse updateStock(Long idProduct, UpdateStockRequest updateStockRequest) throws UnAuthorizedException, ResourceNotFoundException;

}
