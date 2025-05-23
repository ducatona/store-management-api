package com.purchase.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purchase.model.dto.request.OrderRequest;
import com.purchase.repository.IOrderRepository;
import com.purchase.repository.Order;
import com.purchase.security.JwtFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@SpringBootTest()
@AutoConfigureMockMvc
class PurchaseControllerImplTest {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IOrderRepository repository;

    @MockitoBean
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        Order firstOrder = new Order();
        firstOrder.setUserId(1l);
        firstOrder.setProductId(1l);
        firstOrder.setProductTypeId(1l);
        firstOrder.setQuantity(1);
        repository.save(firstOrder);
        Order secondOrder = new Order();
        secondOrder.setUserId(2l);
        secondOrder.setProductId(2l);
        secondOrder.setProductTypeId(1l);
        secondOrder.setQuantity(2);
        repository.save(secondOrder);


    }

    @Test
    void getPurchaseById_whenGivenExistingId_returnsPurchase() throws Exception {
        long id = 5l;


        mockMvc.perform(get("/api/v1/purchase/{id}", id).header("Authorization", "Bearer "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productTypeId", is(1)))
                .andExpect(jsonPath("$.quantity", is(1)))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void getAllPurchases_returnListOfPurchase() throws Exception {

        mockMvc.perform(get("/api/v1/purchase/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getUserPurchase_whenGivenExistingId_returnsPurchase() throws Exception {

        long id = 1l;

        mockMvc.perform(get("/api/v1/purchase/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productTypeId", is(1)))
                .andExpect(jsonPath("$[0].quantity", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)));
    }



    @Test
    void getPurchaseById_whenGivenInvalidId_throwNotFoundException() throws Exception {
        long id = 7l;
        mockMvc.perform(get("/api/v1/purchase/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Not order found with id: " + id)));

    }

    @Test
    void getPurchaseByProductType_whenGivenExistingId_returnsPurchase() throws Exception {
        long id = 1l;

        mockMvc.perform(get("/api/v1/purchase/productType/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productTypeId", is(1)))
                .andExpect(jsonPath("$[0].quantity", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)));
    }


    @Test
    void createPurchase_whenGivenCorrectValues_returnsPurchaseCreated() throws Exception {

        OrderRequest request = new OrderRequest(1l,2l,1l,2);

        String requestAsString = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/purchase/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsString)
                        .with(user("admin").roles("ADMIN")))
                        .andExpect(status().isCreated());
    }
    @Test
    void createPurchase_whenGivenInvalidUser_return404() throws Exception {

        OrderRequest request = new OrderRequest(9l,1l,1l,2);

        String requestAsString = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/purchase/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsString))
                .andExpect(status().isNotFound());
    }
    @Test
    void createPurchase_whenGivenInvalidProductId_return404() throws Exception {

        OrderRequest request = new OrderRequest(1l,6l,1l,2);

        String requestAsString = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/purchase/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsString))
                .andExpect(status().isNotFound());
    }
    @Test
    void createPurchase_whenGivenInvalidType_return404() throws Exception {

        OrderRequest request = new OrderRequest(1l,1l,6l,2);

        String requestAsString = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/purchase/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsString))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPurchase_whenNegativeQuantity_return400() throws Exception {

        OrderRequest request = new OrderRequest(1l,1l,6l,-2);

        String requestAsString = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/purchase/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsString))
                .andExpect(status().isBadRequest());
    }
    @Test
    void createPurchase_whenNegativeToMuchQuantity_return400() throws Exception {

        OrderRequest request = new OrderRequest(1l,1l,1l,9887);

        String requestAsString = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/purchase/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsString))
                        .andExpect(status().isBadRequest());
    }





}