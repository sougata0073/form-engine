package com.sougata.auth_service.service;

import com.sougata.auth_service.dto.UserSummariesShortDto;
import com.sougata.auth_service.dto.UserSummaryShortDto;
import com.sougata.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final String USER_SUMMARY_SHORT_CACHE_KEY_PREFIX = "auth::userSummaryShort::";

    @Value("${app.cache.default-ttl-minutes}")
    private long cacheDefaultTtlMinutes;

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RedisTemplate<String, Object> redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("unchecked")
    public UserSummariesShortDto getUserSummariesShort(List<UUID> userIds) {

        var cacheKeys = userIds.stream().map(id -> USER_SUMMARY_SHORT_CACHE_KEY_PREFIX + id.toString()).toList();
        var cachedUsers = redisTemplate.opsForValue().multiGet(cacheKeys);

        var dbUserIds = new ArrayList<UUID>();

        for (int i = 0; i < cachedUsers.size(); i++) {
            if (cachedUsers.get(i) == null) {
                dbUserIds.add(userIds.get(i));
            }
        }

        var dbUsers = userRepository.getUserSummariesShort(dbUserIds);

        var keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        var valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();

        redisTemplate.executePipelined((RedisCallback<?>) connection -> {

            dbUsers.forEach(user -> {
                byte[] key = keySerializer.serialize(USER_SUMMARY_SHORT_CACHE_KEY_PREFIX + user.getId().toString());
                byte[] value = valueSerializer.serialize(user);

                connection.stringCommands().set(
                        key,
                        value,
                        Expiration.seconds(cacheDefaultTtlMinutes * 60),
                        RedisStringCommands.SetOption.UPSERT
                );
            });

            return null;
        });

        var combinedMap = new HashMap<UUID, UserSummaryShortDto>();

        cachedUsers.forEach(user -> {
            if (user instanceof UserSummaryShortDto u) {
                combinedMap.put(u.getId(), u);
            }
        });

        dbUsers.forEach(user -> {
            combinedMap.put(user.getId(), user);
        });

        var users = userIds.stream().map(combinedMap::get).filter(Objects::nonNull).toList();

        return new UserSummariesShortDto(users);
    }
}
