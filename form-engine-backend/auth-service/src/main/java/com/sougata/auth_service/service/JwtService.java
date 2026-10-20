package com.sougata.auth_service.service;

import com.sougata.auth_service.config.AppConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final AppConfiguration appConfiguration;

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
                .setExpiration(new Date(System.currentTimeMillis() + appConfiguration.getJwtExpirationMillis()))
                .signWith(getJwtSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getJwtSecretKey() {
        return Keys.hmacShaKeyFor(appConfiguration.getJwtSecretString().getBytes());
    }

}
