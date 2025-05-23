package com.auth.security.controller;

import com.auth.security.model.request.AuthRequest;

import com.auth.security.model.response.AuthResponse;
import com.auth.security.model.response.ValidateResponse;
import com.auth.security.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.authenticate(authRequest));
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidateResponse> validateToken(@RequestHeader("Authorization") String authHeader) throws Exception {
        return ResponseEntity.ok(authService.validateToken(authHeader));
    }


}



