package com.auth.security.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API Error Structure")
public class ApiError {


    @Schema(description = "Error code")
    private int status;
    @Schema(description = "Error Message")
    private String message;
    @Schema(description = "Additional details")
    private String details;

    public ApiError(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
