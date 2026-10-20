package com.sougata.gateway_service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppConfiguration {

    private Security security = new Security();
    private Cache cache = new Cache();
    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Security {
        private List<String> publicUrls;
    }

    @Getter
    @Setter
    public static class Cache {
        private Long defaultTtlMinutes;
    }

    @Getter
    @Setter
    public static class Jwt {
        private String secretString;
        private Long expirationMillis;
    }

}
