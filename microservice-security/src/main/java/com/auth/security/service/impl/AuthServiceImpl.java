package com.auth.security.service.impl;

import com.auth.security.client.UserClient;
import com.auth.security.model.request.AuthRequest;
import com.auth.security.model.response.AuthResponse;
import com.auth.security.model.response.UserResponseAuth;
import com.auth.security.model.response.ValidateResponse;
import com.auth.security.security.JwtUtil;
import com.auth.security.service.IAuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {


    private final JwtUtil jwtUtil;
    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(JwtUtil jwtUtil, UserClient userClient, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(AuthRequest authRequest) {

        String email = authRequest.getEmail();
        String password = authRequest.getPassword();

        UserResponseAuth user = userClient.getUserByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getId(), email, user.getRole());

        return new AuthResponse(token);
    }


    public ValidateResponse validateToken(String authHeader) throws Exception {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            return jwtUtil.validateToken(token, jwtUtil.extractEmail(token));
        }
        throw new Exception();
    }


    }



