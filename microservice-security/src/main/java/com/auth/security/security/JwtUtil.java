package com.auth.security.security;

import com.auth.security.model.response.ValidateResponse;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;


    public String generateToken(Long idUser, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("id", idUser)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims validateTokenParser(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("El token ha expirado");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Token JWT no soportado");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token JWT mal formado");
        } catch (SignatureException e) {
            throw new RuntimeException("Firma JWT no válida");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("El token no puede estar vacío");
        }
    }

    public Long extractId(String token) {
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }


    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public ValidateResponse validateToken(String token, String idUser) {

        Claims claims = validateTokenParser(token);

        claims.put("id", extractId(token));
        claims.put("email", extractEmail(token));
        claims.put("role", extractRole(token));


        Long id = claims.get("id", Long.class);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        return new ValidateResponse(id, email, role);
    }

}
