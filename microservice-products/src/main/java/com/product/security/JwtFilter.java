package com.product.security;

import com.product.model.dto.response.ValidateResponse;
import com.product.service.client.AuthClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;

@EnableMethodSecurity(prePostEnabled = true)
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthClient authClient;

    public JwtFilter(AuthClient authClient) {
        this.authClient = authClient;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            ValidateResponse validateResponse = authClient.validateToken(authHeader);

            String role = validateResponse.getRole();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            validateResponse.getEmail(),
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println("No Authorization header found or doesn't start with Bearer.");
        }

        filterChain.doFilter(request, response);
    }


}
