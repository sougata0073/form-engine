package com.sougata.form_engine.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.function.Function;

public class JwtUtil {

    public static <T> T extractData(String jwtSecret, String jwt, Function<Claims, T> function) {
        var claims = Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return function.apply(claims);
    }

}
