package com.sougata.form_response_service.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfiguration {

    private final Long cacheDefaultTtlMinutes;

    public AppConfiguration(
            @Value("${app.cache.default-ttl-minutes}") Long cacheDefaultTtlMinutes
    ) {
        this.cacheDefaultTtlMinutes = cacheDefaultTtlMinutes;
    }

}
