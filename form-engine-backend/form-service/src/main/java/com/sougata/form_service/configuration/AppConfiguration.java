package com.sougata.form_service.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@Getter
public class AppConfiguration {

    private final Long cacheDefaultTtlMinutes;

    public AppConfiguration(
            @Value("${app.cache.default-ttl-minutes}") Long cacheDefaultTtlMinutes
    ) {
        this.cacheDefaultTtlMinutes = cacheDefaultTtlMinutes;
    }

    @Bean
    public Executor cacheLoaderExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("cache-loader-");

        executor.initialize();

        return executor;
    }

}
