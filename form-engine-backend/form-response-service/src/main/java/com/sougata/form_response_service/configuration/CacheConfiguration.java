package com.sougata.form_response_service.configuration;

import com.sougata.form_engine.constant.cache.CommonCacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CacheConfiguration {

    private final AppConfiguration appConfiguration;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, GenericJacksonJsonRedisSerializer redisSerializer) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()))
                .disableCachingNullValues()
                .prefixCacheNameWith(CommonCacheNames.FORM_RESPONSE_SERVICE_PREFIX + CommonCacheNames.SEPARATOR)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));

        Map<String, RedisCacheConfiguration> perCacheConfig = new HashMap<>();

//        perCacheConfig.put(CacheNames.USERS, defaultConfig.entryTtl(Duration.ofMinutes(30)));
//        perCacheConfig.put(CacheNames.PRODUCTS, defaultConfig.entryTtl(Duration.ofHours(2)));
//        perCacheConfig.put(CacheNames.SEARCH_RESULTS, defaultConfig.entryTtl(Duration.ofMinutes(2)));
//        perCacheConfig.put(CacheNames.STATIC_LOOKUPS, defaultConfig.entryTtl(Duration.ofHours(24)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(perCacheConfig)
                .transactionAware()
                .build();
    }

}
