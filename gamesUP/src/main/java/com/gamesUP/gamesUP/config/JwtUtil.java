package com.gamesUP.gamesUP.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "gamesup_secret"; // à sécuriser en prod
    private static final long EXPIRATION_TIME = 864_000_00; // 1 jour

    public String generateToken(String username, String role) {
        return JWT.create()
            .withSubject(username)
            .withClaim("role", role)
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SECRET));
    }

    public String getUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET))
            .build()
            .verify(token)
            .getSubject();
    }

    public String extractUsername(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET))
            .build()
            .verify(token)
            .getSubject();
    }

    public String getRoleFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET))
            .build()
            .verify(token)
            .getClaim("role")
            .asString();
    }
}
