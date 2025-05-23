package com.product.controller.impl;

import com.product.exception.NegativeAmountException;
import com.product.model.dto.request.ProductRequest;
import com.product.model.dto.request.UpdateStockRequest;
import com.product.model.dto.response.ProductResponse;
import com.product.exception.ResourceNotFoundException;
import com.product.security.JwtFilter;
import com.product.service.impl.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductControllerImpl.class)
class ProductControllerImplTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductServiceImpl productService;

    @MockitoBean
    private JwtFilter jwtFilter;


    @Test
    void getAllProducts_whenProductExists_returnListOfProducts() throws Exception {

        List<ProductResponse> productsList = new ArrayList<>();
        productsList.add(new ProductResponse(1l, "Laptop", 1200.0, 10, 1l));
        productsList.add(new ProductResponse(2l, "Smartphone", 800.0, 20, 1l));
        productsList.add(new ProductResponse(3l, "Apple", 1.0, 100, 2l));
        productsList.add(new ProductResponse(4l, "Milk", 0.8, 50, 2l));
        System.out.println(productsList.toString());

        Mockito.when(productService.getAllProducts()).thenReturn(productsList);

        ResultActions response = mockMvc.perform(get("/api/v1/product"));


        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(1200.0))
                .andExpect(jsonPath("$[0].stock").value(10))
                .andExpect(jsonPath("$[0].productTypeId").value(1l))
                .andExpect(jsonPath("$[1].name").value("Smartphone"))
                .andExpect(jsonPath("$[1].price").value(800.0))
                .andExpect(jsonPath("$[1].stock").value(20))
                .andExpect(jsonPath("$[1].productTypeId").value(1l))
                .andExpect(jsonPath("$[2].name").value("Apple"))
                .andExpect(jsonPath("$[2].price").value(1.0))
                .andExpect(jsonPath("$[2].stock").value(100))
                .andExpect(jsonPath("$[2].productTypeId").value(2l))
                .andExpect(jsonPath("$[3].name").value("Milk"))
                .andExpect(jsonPath("$[3].price").value(0.8))
                .andExpect(jsonPath("$[3].stock").value(50))
                .andExpect(jsonPath("$[3].productTypeId").value(2l));


    }

    @Test
    void getProductById_whenGetProductById_returnProductObject() throws Exception {

        long productId = 1l;


        Mockito.when(productService.getProductById(productId)).thenReturn(Optional.of(new ProductResponse(1l, "Laptop", 1200.0, 10, 1l)));

        ResultActions perform = mockMvc.perform(get("/api/v1/product/{id}", productId));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1200.0))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.productTypeId").value(1l));


    }


    @Test
    void getProductById_whenGivenInvalidID_returnNotFoundException() throws Exception {

        long productId = 8;

        Mockito.when(productService.getProductById(productId)).thenThrow(new ResourceNotFoundException("No product found with the id: " + productId));

        mockMvc.perform(get("/api/v1/product/{id}", productId)).andExpect(status().isNotFound());

    }


    @Test
    void createProduct_givenProductObject_thenReturnSavedProduct() throws Exception {


        ProductRequest productRequest = new ProductRequest("TV", 200.2, 10, 1L);

        ProductResponse productResponse = new ProductResponse(5L, "TV", 200.2, 10, 1L);


        Mockito.when(productService.createProduct(Mockito.any(ProductRequest.class))).thenReturn(productResponse);

        String requestJson = objectMapper.writeValueAsString(productRequest);


        ResultActions perform = mockMvc.perform(post("/api/v1/product")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson)
                .with(user("admin").roles("ADMIN"))
        );

        perform
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TV"))
                .andExpect(jsonPath("$.price").value(200.2))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.productTypeId").value(1L));

    }


    @Test
    void createProduct_givenProductObject_thenReturnBadRequest() throws Exception {

        ProductRequest productRequest = new ProductRequest("", 2.2, 1, 1L);

        String jsonTransformed = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON).content(jsonTransformed))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name:product name is required"));
    }


    @Test
    void updateProduct_updateExistingProductGivenId_thenReturnUpdateProduct() throws Exception {
        long idProduct = 1L;

        ProductRequest productToUpdate = new ProductRequest("MTV", 3.50, 4, 1L);
        ProductResponse productResponse = new ProductResponse(idProduct, "MTV", 3.50, 4, 1l);

        Mockito.when(productService.updateProduct(eq(idProduct), Mockito.any(ProductRequest.class)))
                .thenReturn(Optional.of(productResponse));

        String jsonFormat = objectMapper.writeValueAsString(productToUpdate);

        mockMvc.perform(put("/api/v1/product/{id}", idProduct).contentType(MediaType.APPLICATION_JSON).content(jsonFormat))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MTV"))
                .andExpect(jsonPath("$.price").value(3.50))
                .andExpect(jsonPath("$.stock").value(4))
                .andExpect(jsonPath("$.productTypeId").value(1l));

    }

    @Test
    void updateProduct_updateExistingProductGivenId_thenReturnNotFoundException() throws Exception {
        long idProduct = 900;
        ProductRequest productToUpdate = new ProductRequest("MTV", 3.50, 4, 1L);
        Mockito.when(productService.updateProduct(idProduct, productToUpdate)).thenThrow(new ResourceNotFoundException("There is no product to update with ID: " + idProduct));
    }

    @Test
    void updateProduct_updateExistingProductGivenId_thenReturnBadRequestException() throws Exception {

        long idProduct = 1;

        ProductRequest productToUpdate = new ProductRequest("MTV", -2.2, 4, 1L);

        Mockito.doThrow(new ResourceNotFoundException("")).when(productService).updateProduct(idProduct, productToUpdate);

        String f = objectMapper.writeValueAsString(productToUpdate);

        mockMvc.perform(put("/api/v1/product/{id}", idProduct).contentType(MediaType.APPLICATION_JSON).content(f)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("price:The price cannot be negative"));

    }


    @Test
    void deleteProduct_whenGivenId_deleteProduct() throws Exception {

        long idProduct = 1L;

        Mockito.doNothing().when(productService).deleteProduct(idProduct);

        mockMvc.perform(delete("/api/v1/product/{id}", idProduct)).andExpect(status().isOk());

        Mockito.verify(productService, Mockito.times(1)).deleteProduct(idProduct);


    }

    @Test
    void deleteProduct_whenGivenId_notFoundProduct() throws Exception {

        long idProduct = 80L;

        Mockito.doThrow(new ResourceNotFoundException("The product cannot be deleted with the id: " + idProduct)).when(productService).deleteProduct(idProduct);

        mockMvc.perform(delete("/api/v1/product/{id}", idProduct)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("The product cannot be deleted with the id: 80"));

        Mockito.verify(productService, Mockito.times(1)).deleteProduct(idProduct);
    }



    @Test
    void reduceStock_whenGiveIdProductAndAmount_updateAmountOfStock() throws Exception {

        long idProduct = 1l;
        int amount = 6;
        UpdateStockRequest request = new UpdateStockRequest(amount);


        Mockito.doNothing().when(productService).reduceStock(idProduct,request);
        mockMvc.perform(put("/api/v1/product/{id}/stock/{amount}",idProduct,amount)).andExpect(status().isOk());

    }

    @Test
    void reduceStock_whenAmountIsNegative_throwNegativeAmountException() throws Exception {

        long idProduct = 1l;
        int amount = -1999;
        UpdateStockRequest request = new UpdateStockRequest(amount);

        Mockito.doThrow(new NegativeAmountException("The amount to reduce cannot be negative.")).when(productService).reduceStock(idProduct,request);

        mockMvc.perform(put("/api/v1/product/{id}/stock/{amount}",idProduct,amount)).andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.message").value("The amount to reduce cannot be negative."));

    }

    @Test
    void reduceStock_whenAmountIsElder_throwNegativeAmountException() throws Exception {

        long idProduct = 1l;
        int amount = 1999;
        UpdateStockRequest request = new UpdateStockRequest(amount);
        Mockito.doThrow(new NegativeAmountException("Not enough stock to reduce by the specified amount.")).when(productService).reduceStock(idProduct,request);

        mockMvc.perform(put("/api/v1/product/{id}/stock/{amount}",idProduct,amount)).andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.message").value("Not enough stock to reduce by the specified amount."));

    }

}





















