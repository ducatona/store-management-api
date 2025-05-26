package com.product.controller;

import com.product.model.dto.request.ProductTypeRequest;
import com.product.model.dto.response.ProductResponse;
import com.product.model.dto.response.ProductTypeResponse;
import com.product.exception.ApiError;
import com.product.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;
@OpenAPIDefinition(info = @Info(title = "API Documentation",version = "1.0", description = "API dedicated to ProductType management"),security = @SecurityRequirement(name = "bearerAuth"))
@Tag(name = "ProductType Management", description = "API for managing productTypes")
public interface IProductTypeController {


    @Operation(summary = "Get all ProductsTypes", description = "ProductsTypes retrieved successfully")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProductType find successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    })
    ResponseEntity<List<ProductTypeResponse>> getAllProducts();

    @Operation(summary = "Get product by ID", description = "Retrieve productType details using their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProductType found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "ProductType not found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500",description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiError.class))),
    })
    ResponseEntity<Optional<ProductTypeResponse>> getProductById(Long id) throws ResourceNotFoundException;

    @Operation(summary = "Create a new productType", description = "Add a new productType to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProductType created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "ProductType not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    ResponseEntity<ProductTypeResponse> createProductType(ProductTypeRequest productTypeRequest) throws ResourceNotFoundException;

    @Operation(summary = "Update a productType", description = "Update details of an existing product type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProductType updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "ProductType not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    ResponseEntity<Optional<ProductTypeResponse>> updateProductType(Long id, ProductTypeRequest productTypeRequest) throws ResourceNotFoundException;
    @Operation(summary = "Delete a productType", description = "Delete a productType from the system using their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProductType deleted successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "ProductType not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    void deleteProductType(Long id) throws ResourceNotFoundException;
}
