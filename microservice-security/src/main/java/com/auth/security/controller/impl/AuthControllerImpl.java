package com.auth.security.controller.impl;

import com.auth.security.controller.IAuthController;
import com.auth.security.model.request.AuthRequest;

import com.auth.security.model.response.AuthResponse;
import com.auth.security.model.response.ValidateResponse;
import com.auth.security.service.impl.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthControllerImpl implements IAuthController {

    private final AuthServiceImpl authServiceImpl;

    public AuthControllerImpl(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authServiceImpl.authenticate(authRequest));
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidateResponse> validateToken(@RequestHeader("Authorization") String authHeader) throws Exception {
        return ResponseEntity.ok(authServiceImpl.validateToken(authHeader));
    }


}



