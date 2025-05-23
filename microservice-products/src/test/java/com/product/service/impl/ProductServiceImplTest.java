package com.product.service.impl;

import com.product.exception.NegativeAmountException;
import com.product.exception.UnAuthorizedException;
import com.product.model.dto.request.ProductRequest;
import com.product.model.dto.request.UpdateStockRequest;
import com.product.model.dto.response.ProductResponse;
import com.product.exception.ResourceNotFoundException;
import com.product.model.dto.response.UpdateStockResponse;
import com.product.repository.Product;
import com.product.repository.ProductType;
import com.product.repository.IProductRepository;
import com.product.repository.IProductTypeRepository;
import com.product.service.client.UserClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

import java.util.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    IProductRepository repository;

    @Mock
    IProductTypeRepository repositoryType;
    @Mock
    UserClient userClient;
    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    ProductServiceImpl service;

    private ProductResponse productResponse;
    private ProductResponse response;
    private ProductType productType;
    private Product product;
    private Product productUpdated;
    private ProductRequest productRequest;
    private ProductRequest request;
    private ProductType newType;

    @BeforeEach
    public void setUp() {

        productType = new ProductType();
        productType.setId(1L);
        productType.setName("ProductType");


        product = new Product();
        product.setId(1l);
        product.setName("product");
        product.setPrice(2.1);
        product.setStock(10);
        product.setProductType(productType);

        productRequest = new ProductRequest();
        productRequest.setName("product");
        productRequest.setPrice(2.1);
        productRequest.setStock(10);
        productRequest.setProductTypeId(1L);


        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("product");
        productResponse.setPrice(2.1);
        productResponse.setStock(10);
        productResponse.setProductTypeId(1L);


        newType = new ProductType();
        newType.setName("newType");
        newType.setId(2l);

        productUpdated = new Product();
        productUpdated.setId(1L);
        productUpdated.setName("pepe");
        productUpdated.setPrice(2.1);
        productUpdated.setStock(1);
        productUpdated.setProductType(newType);


        request = new ProductRequest();
        request.setName("newProduct");
        request.setPrice(10.0);
        request.setStock(10);
        request.setProductTypeId(2l);

        response = new ProductResponse();
        response.setId(1l);
        response.setName("newProduct");
        response.setPrice(10.0);
        response.setStock(10);
        response.setProductTypeId(2l);

    }

    @Test
    void givenProductList_whenGetAllProducts_thenReturnProductList() {

        Mockito.when(repository.findAll()).thenReturn(List.of(product));
        Mockito.when(modelMapper.map(product, ProductResponse.class)).thenReturn(productResponse);

        List<ProductResponse> actual = service.getAllProducts();

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("product", actual.get(0).getName());
        Assertions.assertEquals(2.1, actual.get(0).getPrice());
        Assertions.assertEquals(10, actual.get(0).getStock());
        Assertions.assertEquals(1L, actual.get(0).getProductTypeId());


        Mockito.verify(modelMapper).map(product, ProductResponse.class);
        Mockito.verify(repository).findAll();

    }

    @Test
    void getProductById_whenGetProductGivenId_thenReturnProductObjectDetails() throws ResourceNotFoundException {

        long productId = 1l;

        Mockito.when(repository.findById(productId)).thenReturn(Optional.ofNullable(product));
        Mockito.when(modelMapper.map(product, ProductResponse.class)).thenReturn(productResponse);

        Optional<ProductResponse> actual = service.getProductById(productId);

        Assertions.assertEquals("product", actual.get().getName());
        Assertions.assertEquals(2.1, actual.get().getPrice());
        Assertions.assertEquals(10, actual.get().getStock());
        Assertions.assertEquals(1L, actual.get().getProductTypeId());

        Mockito.verify(repository).findById(productId);
    }

    @Test
    void getProductById_whenGetProductGivenId_thenReturnNotFoundException() throws ResourceNotFoundException {

        long productId = 2L;

        Mockito.when(repository.findById(productId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getProductById(productId));

        Assertions.assertEquals("No product found with the id: 2", exception.getMessage());

        Mockito.verify(repository, Mockito.times(1)).findById(productId);

    }


    @Test
    void createProduct_whenProductIsCreated_thenReturnProductObject() throws ResourceNotFoundException {

        long typeId = 1l;

        Mockito.when(repositoryType.findById(typeId)).thenReturn(Optional.of(productType));
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(product);
        Mockito.when(modelMapper.map(product, ProductResponse.class)).thenReturn(productResponse);

        ProductResponse actual = service.createProduct(productRequest);

        Assertions.assertEquals("product", actual.getName());

    }


    @Test
    void updateProduct_whenProductExists_thenReturnTheObjectUpdated() throws ResourceNotFoundException, UnAuthorizedException {
        long id = 1L;
        long type = 2L;



        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(product));
        Mockito.when(repositoryType.findById(type)).thenReturn(Optional.of(newType));
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(productUpdated);
        Mockito.when(modelMapper.map(Mockito.any(Optional.class), Mockito.eq(ProductResponse.class))).thenReturn(response);

        Optional<ProductResponse> actual = service.updateProduct(id, request);
        Assertions.assertEquals(1l, actual.get().getId());
        Assertions.assertEquals("newProduct", actual.get().getName());
        Assertions.assertEquals(10.0, actual.get().getPrice());
        Assertions.assertEquals(10, actual.get().getStock());
        Assertions.assertEquals(2l, actual.get().getProductTypeId());

    }

    @Test
    void updateProduct_whenProductExists_thenReturnNotFoundException() throws ResourceNotFoundException {
        long id = 8L;
        long type = 2L;


        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(product));

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.updateProduct(id, request));

    }

    @Test
    void updateProduct_whenProductTypeDoesNotExist_thenThrowResourceNotFoundException() {

        long productId = 1L;
        long productTypeId = 9L;
        long idUser = 2L;
        ProductRequest request = new ProductRequest();
        request.setProductTypeId(productTypeId);

        Product existingProduct = new Product();


        Mockito.when(repository.findById(productId)).thenReturn(Optional.of(existingProduct));
        Mockito.when(repositoryType.findById(productTypeId)).thenReturn(Optional.empty());


        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.updateProduct(productId, request);
        });


        Assertions.assertEquals("Product type not found with ID: " + productTypeId, ex.getMessage());
    }


    @Test
    void updateProduct_whenProductDoesNotExist_thenThrowResourceNotFoundException() {

        long productId = 1L;
        long idUser = 2L;
        ProductRequest request = new ProductRequest();
        Mockito.when(repository.findById(productId)).thenReturn(Optional.empty());


        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.updateProduct(productId, request);
        });

        Assertions.assertEquals("There is no product to update with ID:" + productId, ex.getMessage());
    }

    @Test
    void deleteProduct_whenGivenId_deleteProduct() throws ResourceNotFoundException {

        Mockito.when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        service.deleteProduct(product.getId());

        Mockito.verify(repository).deleteById(product.getId());

    }

    @Test
    void deleteProduct_whenGivenId_notFoundProduct() {

        long id = 5L;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.deleteProduct(id));

        Assertions.assertEquals("The product cannot be deleted with the id: 5", exception.getMessage());
    }


    @Test
    void updateStock_whenUserAdmin_returnStockUpdated() throws UnAuthorizedException, ResourceNotFoundException {

        Long id = 1L;
        Integer stock = 50;

        UpdateStockResponse newResponse = new UpdateStockResponse();
        newResponse.setStock(stock);

        UpdateStockRequest request = new UpdateStockRequest();
        request.setNewStock(stock);



        Product newProductUpdated = new Product();
        newProductUpdated.setStock(stock);


        Mockito.when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(newProductUpdated);
        Mockito.when(modelMapper.map(Mockito.any(Product.class), Mockito.eq(UpdateStockResponse.class))).thenReturn(newResponse);


        UpdateStockResponse actual = service.updateStock(id,request);


        Assertions.assertEquals(stock, actual.getStock());


    }

    @Test
    void reduceStock_whenGiveCorrectId_reduceProduct() throws NegativeAmountException, ResourceNotFoundException {

        int amountToReduce = 1;
        long idProduct =1l;
        UpdateStockRequest request = new UpdateStockRequest(amountToReduce);
        ProductType newProductType = new ProductType();
        newProductType.setId(1l);
        newProductType.setName("newType");

        Product productToReduce = new Product();
        productToReduce.setId(idProduct);
        productToReduce.setName("Apples");
        productToReduce.setStock(100);
        productToReduce.setProductType(newProductType);


        Mockito.when(repository.findById(idProduct)).thenReturn(Optional.of(productToReduce));

        service.reduceStock(idProduct,request);


        Mockito.verify(repository,Mockito.times(1)).findById(idProduct);

    }







}




