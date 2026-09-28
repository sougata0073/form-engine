package com.sougata.form_data_service.initiator;

import com.sougata.form_engine.constant.RedisConsumerGroupNames;
import com.sougata.form_engine.constant.RedisStreamNames;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisStreamConsumerGroupInitiator implements ApplicationRunner {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Map<String, Set<String>> STREAM_NAME_AND_GROUPS = Map.of(
            RedisStreamNames.FORM_RESPONSE_STREAM,
            Set.of(RedisConsumerGroupNames.FORM_RESPONSE_CONSUMER)
    );

    @Override
    public void run(@NonNull ApplicationArguments args) {

        STREAM_NAME_AND_GROUPS.forEach((streamName, consumerGroups) -> {
            consumerGroups.forEach(consumerGroup -> {
                try {
                    redisTemplate.opsForStream().createGroup(streamName, consumerGroup);
                } catch (RuntimeException ignored) {

                }
            });
        });

    }
}
