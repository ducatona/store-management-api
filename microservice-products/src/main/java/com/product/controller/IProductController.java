package com.product.controller;

import com.product.exception.NegativeAmountException;
import com.product.exception.UnAuthorizedException;
import com.product.model.dto.request.ProductRequest;
import com.product.model.dto.request.UpdateStockRequest;
import com.product.model.dto.response.ProductResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@OpenAPIDefinition(info = @Info(title = "API Documentation",version = "1.0", description = "API dedicated to product management"),security = @SecurityRequirement(name = "bearerAuth"))
@Tag(name = "Product Management", description = "API for managing product")
public interface IProductController {


    @Operation(summary = "Get all Products", description = "Products retrieved successfully")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product find successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    })
    ResponseEntity<List<ProductResponse>> getAllProducts();


    @Operation(summary = "Get product by ID", description = "Retrieve product's details using their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    ResponseEntity<Optional<ProductResponse>> getProductById(Long id) throws ResourceNotFoundException;

    @Operation(summary = "Create a new product", description = "Add a new product to the system")
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
    ResponseEntity<ProductResponse> createProduct(ProductRequest productRequest) throws ResourceNotFoundException;


    @Operation(summary = "Update a product", description = "Update an existing product's details ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    ResponseEntity<Optional<ProductResponse>> updateProduct(Long idProduct, ProductRequest productRequest) throws ResourceNotFoundException, UnAuthorizedException;

    @Operation(summary = "Delete a product", description = "Delete a product from the system using their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    void deleteProduct(Long id) throws ResourceNotFoundException;
    @Operation(summary = "Reduce Stock", description = "Reduce stock of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product stock reduced successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product to reduce not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    void reduceStock(@PathVariable Long id, @RequestBody UpdateStockRequest updateStockRequest) throws ResourceNotFoundException, NegativeAmountException;
}
