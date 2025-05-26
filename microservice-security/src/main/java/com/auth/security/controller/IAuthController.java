package com.auth.security.controller;

import com.auth.security.exception.ApiError;
import com.auth.security.model.request.AuthRequest;
import com.auth.security.model.response.AuthResponse;
import com.auth.security.model.response.ValidateResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@OpenAPIDefinition(info = @Info(title = "API Documentation",version = "1.0", description = "API dedicated to ProductType management"),security = @SecurityRequirement(name = "bearerAuth"))
@Tag(name = "ProductType Management", description = "API for managing productTypes")
public interface IAuthController {

    @Operation(summary = "Login user", description = "Token of user returned successfully")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User register successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "404", description = "Email not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
    })
    ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest);

    @Operation(summary = "Validate Token", description = "Token validate successfully")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token validate successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    })
    ResponseEntity<ValidateResponse> validateToken(@RequestHeader("Authorization") String authHeader) throws Exception ;

}
