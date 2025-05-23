package com.product.service;

import com.product.model.dto.request.ProductTypeRequest;
import com.product.model.dto.response.ProductTypeResponse;
import com.product.exception.ResourceNotFoundException;


import java.util.List;
import java.util.Optional;

public interface IProductTypeService {



    List<ProductTypeResponse> getAllProductsTypes();
    Optional<ProductTypeResponse> getProductTypeById(Long id) throws ResourceNotFoundException;
    ProductTypeResponse createProductType(ProductTypeRequest productTypeRequest) throws ResourceNotFoundException;
    Optional<ProductTypeResponse> updateProductType(Long id, ProductTypeRequest productTypeRequest) throws ResourceNotFoundException;
    void deleteProductType(Long id) throws ResourceNotFoundException;

}
