package com.sougata.auth_service.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret-string}")
    private String jwtSecretString;

    @Value("${jwt.expiration-millis}")
    private Long jwtExpirationMillis;

    public String generateJwtToken(String id, String name, String email, String avatarUrl) {

        Map<String, Object> claims = Map.of(
                "name", name == null ? "" : name,
                "email", email == null ? "" : email,
                "avatarUrl", avatarUrl == null ? "" : avatarUrl
        );

        return Jwts
                .builder()
                .setSubject(id)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMillis))
                .signWith(getJwtSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getJwtSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretString.getBytes());
    }

}
