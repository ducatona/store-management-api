package com.product.controller.impl;


import com.product.model.dto.request.ProductTypeRequest;
import com.product.model.dto.response.ProductTypeResponse;
import com.product.exception.ResourceNotFoundException;
import com.product.security.JwtFilter;
import com.product.security.SecurityConfig;
import com.product.service.impl.ProductTypeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class ProductTypeControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private ProductTypeServiceImpl productTypeService;

    @MockitoBean
    private JwtFilter jwtFilter;


    @BeforeEach
    void setUp() throws Exception {
        Mockito.doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtFilter).doFilter(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void createProduct_givenProductTypeObject_thenReturnSavedProductType() throws Exception {

        ProductTypeRequest productTypeRequest = new ProductTypeRequest("Movies");
        ProductTypeResponse productTypeResponse = new ProductTypeResponse(3l, "Movies");

        Mockito.when(productTypeService.createProductType(Mockito.any(ProductTypeRequest.class))).thenReturn(productTypeResponse);

        String requestJson = objectMapper.writeValueAsString(productTypeRequest);

        ResultActions perform = mockMvc.perform(post("/api/v1/productType").with(user("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON).content(requestJson));

        perform
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Movies"));

    }

    @Test
    void getAllProducts_whenProductTypeExists_returnListOfProducts() throws Exception {

        List<ProductTypeResponse> productsList = new ArrayList<>();
        productsList.add(new ProductTypeResponse(1l, "Electronics"));
        productsList.add(new ProductTypeResponse(2l, "Groceries"));


        Mockito.when(productTypeService.getAllProductsTypes()).thenReturn(productsList);


        ResultActions response = mockMvc.perform(get("/api/v1/productType"));


        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Groceries"));


    }

    @Test
    void getProductTypeById_whenGetProductTypeById_returnProductTypeObject() throws Exception {

        long productId = 1l;


        Mockito.when(productTypeService.getProductTypeById(productId)).thenReturn(Optional.of(new ProductTypeResponse(1l, "Electronics")));

        ResultActions perform = mockMvc.perform(get("/api/v1/productType/{id}", productId));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Electronics"));

    }



    @Test
    void createProductType_givenProductTypeObject_thenReturnNotFoundException() throws Exception {

        ProductTypeRequest productTypeRequest = new ProductTypeRequest("");


        String jsonTransformed = objectMapper.writeValueAsString(productTypeRequest);
        mockMvc.perform(post("/api/v1/productType").with(user("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON).content(jsonTransformed))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name:productType name is required"));
    }


    @Test
    void updateProductType_updateExistingProductTypeGivenId_thenReturnUpdateProductType() throws Exception {
        long idProduct = 1L;
        ProductTypeRequest productToUpdate = new ProductTypeRequest("Movies");
        ProductTypeResponse productTypeResponse = new ProductTypeResponse(idProduct, "Movies");

        Mockito.when(productTypeService.updateProductType(eq(idProduct), Mockito.any(ProductTypeRequest.class)))
                .thenReturn(Optional.of(productTypeResponse));

        String jsonFormat = objectMapper.writeValueAsString(productToUpdate);

        mockMvc.perform(put("/api/v1/productType/{id}", idProduct).with(user("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON).content(jsonFormat))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Movies"));


    }

    @Test
    void updateProductType_updateExistingProductTypeGivenId_thenReturnNotFoundException() throws Exception {

        long idProduct = 900;
        ProductTypeRequest productToUpdate = new ProductTypeRequest("Movies");
        Mockito.when(productTypeService.updateProductType(idProduct, productToUpdate)).thenThrow(new ResourceNotFoundException("There is no product to update with ID: " + idProduct));

    }

    @Test
    void updateProductType_updateExistingProductTypeGivenId_thenReturnBadRequestException() throws Exception {

        long idProduct = 1;

        ProductTypeRequest productToUpdate = new ProductTypeRequest("");

        Mockito.doThrow(new ResourceNotFoundException("")).when(productTypeService).updateProductType(idProduct, productToUpdate);

        String f = objectMapper.writeValueAsString(productToUpdate);

        mockMvc.perform(put("/api/v1/productType/{id}", idProduct).with(user("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON).content(f)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name:productType name is required"));

    }


    @Test
    void deleteProductType_whenGivenId_deleteProductType() throws Exception {

        long idProduct = 1L;

        Mockito.doNothing().when(productTypeService).deleteProductType(idProduct);

        mockMvc.perform(delete("/api/v1/productType/{id}", idProduct).with(user("admin").roles("ADMIN"))).andExpect(status().isOk());

        Mockito.verify(productTypeService, Mockito.times(1)).deleteProductType(idProduct);


    }




}