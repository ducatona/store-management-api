package com.purchase.controller;

import com.purchase.exception.ApiError;
import com.purchase.exception.ResourceNotFoundException;
import com.purchase.model.dto.request.OrderRequest;
import com.purchase.model.dto.response.OrderResponse;
import com.purchase.model.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
@OpenAPIDefinition(info = @Info(title = "API Documentation",version = "1.0", description = "API dedicated to purchase management"))
@Tag(name = "Purchase Management", description = "API for managing purchase")
public interface IPurchaseController {


    @Operation(summary = "Create a new purchase", description = "Add a new purchase to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    ResponseEntity<OrderResponse> createPurchase(@RequestBody OrderRequest orderRequest) throws ResourceNotFoundException;

    @Operation(summary = "Get all Purchase", description = "Purchases retrieved successfully")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase find successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    })
    ResponseEntity<List<OrderResponse>> getAllPurchase();

    @Operation(summary = "Get all Purchase by User", description = "Purchase retrieved successfully")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase find successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    })
    ResponseEntity<List<OrderResponse>> getUserPurchase(@PathVariable Long idUser);

    @Operation(summary = "Get purchase by ID", description = "Retrieve purchase's details using their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Purchase not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    ResponseEntity<OrderResponse> getPurchaseById(@PathVariable Long idPurchase) throws ResourceNotFoundException;

    @Operation(summary = "Get al Purchase by ProductType ", description = "Purchase retrieved successfully")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase find successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    })
    ResponseEntity<List<OrderResponse>> getProductTypePurchase(@PathVariable Long id);

}

