package com.sougata.auth_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfiguration {

    private final Long cacheDefaultTtlMinutes;
    private final String jwtSecretString;
    private final Long jwtExpirationMillis;

    public AppConfiguration(
            @Value("${app.cache.default-ttl-minutes}") Long cacheDefaultTtlMinutes,
            @Value("${jwt.secret-string}") String jwtSecretString,
            @Value("${jwt.expiration-millis}") Long jwtExpirationMillis
    ) {
        this.cacheDefaultTtlMinutes = cacheDefaultTtlMinutes;
        this.jwtSecretString = jwtSecretString;
        this.jwtExpirationMillis = jwtExpirationMillis;
    }

}
