package com.sougata.form_service.service.formSchema.impl;

import com.sougata.form_engine.constant.DistributedLockNames;
import com.sougata.form_engine.constant.cache.FormCacheNames;
import com.sougata.form_engine.dto.form.FormDefaultSettingsDto;
import com.sougata.form_engine.dto.form.FormDetailsDto;
import com.sougata.form_service.repository.formSchema.FormDefaultSettingsRepository;
import com.sougata.form_service.service.formSchema.FormServiceAsync;
import com.sougata.form_service.service.formSchema.FormServiceCached;
import com.sougata.form_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FormServiceCachedImpl implements FormServiceCached {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;
    private final FormServiceAsync formServiceAsync;
    private final FormDefaultSettingsRepository formDefaultSettingsRepository;

    private final ConcurrentHashMap<UUID, CompletableFuture<FormDetailsDto>> formDetailsInFlight = new ConcurrentHashMap<>();

    @Override
    public FormDetailsDto getFormDetails(UUID formId) {

        var formDetailsCacheKey = CacheUtil.buildKey(FormCacheNames.FORM_DETAILS, formId);

        var formDetailsCached = (FormDetailsDto) redisTemplate.opsForValue().get(formDetailsCacheKey);

        if (formDetailsCached != null) {
            return formDetailsCached;
        }

        var formDetailsLock = redissonClient.getLock(DistributedLockNames.formDetailsDbLoad(formId));

        var future = formDetailsInFlight.computeIfAbsent(formId, id -> {

            formDetailsLock.lock();

            var formDetailsCachedAgain = (FormDetailsDto) redisTemplate.opsForValue().get(formDetailsCacheKey);

            if (formDetailsCachedAgain != null) {
                return CompletableFuture.completedFuture(formDetailsCachedAgain);
            }

            return formServiceAsync.loadFormDetailsFromDb(id);
        });

        try {

            return future.join();

        } finally {

            formDetailsInFlight.remove(formId, future);

            if (formDetailsLock.isHeldByCurrentThread()) {
                formDetailsLock.unlock();
            }
        }
    }

    @Override
    @Cacheable(cacheNames = {FormCacheNames.FORM_DEFAULT_SETTINGS}, key = "#userId")
    public FormDefaultSettingsDto getDefaultFormSettings(UUID userId) {
        var defSettings = formDefaultSettingsRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Form default settings not found for user ID: " + userId));

        return new FormDefaultSettingsDto(defSettings.getDefaultEmailCollectionOption(), defSettings.getMakeQuestionsRequiredByDefault());
    }
}
