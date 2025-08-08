package com.microservice_auth.common.util;


import com.microservice_auth.feature.user.enums.RoleName;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.microservice_auth.feature.verificationToken.constants.VerificationTokenConstants.EMAIL_TOKEN_EXPIRATION_HOURS;

@Service
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private Key SECRET_KEY;


    private static final long LOGIN_TOKEN_VALIDATION_TIME = 1000L * 60 * 60 * 24 * 14;
    private static final long EMAIL_TOKEN_EXPIRATION_IN_MILLISECONDS = 1000L * 60 * 60 * EMAIL_TOKEN_EXPIRATION_HOURS;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 256 bits (32 bytes)");
        }
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String userId, RoleName roleName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", roleName);
        return createToken(claims, userId, LOGIN_TOKEN_VALIDATION_TIME);
    }

    public String generateEmailVerificationToken(String email) {
        return createToken(new HashMap<>(), email, EMAIL_TOKEN_EXPIRATION_IN_MILLISECONDS);
    }

    private String createToken(Map<String, Object> claims, String subject, long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }


}

