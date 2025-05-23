package com.product.service.impl;

import com.product.model.dto.request.ProductTypeRequest;
import com.product.model.dto.response.ProductTypeResponse;
import com.product.exception.ResourceNotFoundException;
import com.product.repository.ProductType;
import com.product.repository.IProductTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class ProductTypeServiceImplTest {


    @Mock
    IProductTypeRepository repositoryType;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    ProductTypeServiceImpl service;

    private ProductTypeResponse productTypeResponse;
    private ProductType productType;
    private ProductType productTypeUpdated;
    private ProductTypeRequest productTypeRequest;

    @BeforeEach
    public void setUp() {

        productType = new ProductType();
        productType.setId(1L);
        productType.setName("ProductType");

        productTypeResponse = new ProductTypeResponse();
        productTypeResponse.setId(1L);
        productTypeResponse.setName("ProductType");


        productTypeRequest = new ProductTypeRequest();
        productTypeRequest.setName("ProductType");

        productTypeUpdated = new ProductType();
        productTypeUpdated.setName("ProductTypeNew");


    }

    @Test
    void givenProductTypeList_whenGetAllProducts_thenReturnProductList() {

        Mockito.when(repositoryType.findAll()).thenReturn(List.of(productType));
        Mockito.when(modelMapper.map(productType, ProductTypeResponse.class)).thenReturn(productTypeResponse);

        List<ProductTypeResponse> actual = service.getAllProductsTypes();

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("ProductType", actual.get(0).getName());

    }

    @Test
    void getProductById_whenGetProductGivenId_thenReturnProductObjectDetails() throws ResourceNotFoundException {

        long productTypeId = 1L;

        Mockito.when(repositoryType.findById(productTypeId)).thenReturn(Optional.ofNullable(productType));
        Mockito.when(modelMapper.map(productType, ProductTypeResponse.class)).thenReturn(productTypeResponse);

        Optional<ProductTypeResponse> actual = service.getProductTypeById(productTypeId);

        Assertions.assertEquals("ProductType", actual.get().getName());


        Mockito.verify(repositoryType).findById(productTypeId);
    }

    @Test
    void getProductById_whenGetProductGivenId_thenReturnNotFoundException() throws ResourceNotFoundException {

        long productTypeId = 2L;

        Mockito.when(repositoryType.findById(productTypeId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getProductTypeById(productTypeId));

        Assertions.assertEquals("No productType found with the id: 2", exception.getMessage());

        Mockito.verify(repositoryType, Mockito.times(1)).findById(productTypeId);

    }


    @Test
    void createProduct_whenProductIsCreated_thenReturnProductObject() throws ResourceNotFoundException {


        Mockito.when(repositoryType.save(Mockito.any(ProductType.class))).thenReturn(productType);
        Mockito.when(modelMapper.map(Mockito.any(ProductType.class), Mockito.eq(ProductTypeResponse.class))).thenReturn(productTypeResponse);

        ProductTypeResponse actual = service.createProductType(productTypeRequest);

        Assertions.assertEquals("ProductType", actual.getName());

    }


    @Test
    void updateProduct_whenProductExists_thenReturnTheObjectUpdated() throws ResourceNotFoundException {
        long id = 1L;

        Mockito.when(repositoryType.findById(id)).thenReturn(Optional.ofNullable(productType));
        Mockito.when(repositoryType.save(Mockito.any(ProductType.class))).thenReturn(productTypeUpdated);
        Mockito.when(modelMapper.map(Mockito.any(Optional.class), Mockito.eq(ProductTypeResponse.class))).thenReturn(productTypeResponse);

        Optional<ProductTypeResponse> actual = service.updateProductType(id, productTypeRequest);
        Assertions.assertEquals(1l, actual.get().getId());
        Assertions.assertEquals("ProductType", actual.get().getName());

    }

    @Test
    void updateProduct_whenProductExists_thenReturnNotFoundException() throws ResourceNotFoundException {
        long id = 8L;

        Mockito.when(repositoryType.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = Assertions
                .assertThrows(ResourceNotFoundException.class, () -> service.updateProductType(id, productTypeRequest));

        Assertions.assertEquals("Product type not found with ID: 8", ex.getMessage());

    }


    @Test
    void updateProduct_whenProductDoesNotExist_thenThrowResourceNotFoundException() {

        long productId = 1L;

        ProductTypeRequest request = new ProductTypeRequest();

        Mockito.when(repositoryType.findById(productId)).thenReturn(Optional.empty());


        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.updateProductType(productId, request);
        });

        Assertions.assertEquals("Product type not found with ID: " + productId, ex.getMessage());
    }


    @Test
    void deleteProduct_whenGivenId_deleteProduct() throws ResourceNotFoundException {

        Mockito.when(repositoryType.findById(productType.getId())).thenReturn(Optional.of(productType));

        service.deleteProductType(productType.getId());

        Mockito.verify(repositoryType).deleteById(productType.getId());

    }


    @Test
    void deleteProduct_whenGivenId_notFoundProduct() {

        long id = 5L;

        Mockito.when(repositoryType.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.deleteProductType(id));

        Assertions.assertEquals("The productType cannot be deleted with the id: 5", exception.getMessage());


    }

}