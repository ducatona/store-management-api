package com.auth.security.service;

import com.auth.security.model.request.AuthRequest;
import com.auth.security.model.response.AuthResponse;
import com.auth.security.model.response.ValidateResponse;

public interface IAuthService
{


    AuthResponse authenticate(AuthRequest authRequest);
    ValidateResponse validateToken(String authHeader) throws Exception;

}
