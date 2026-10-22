package com.sougata.form_response_service.messaging.handler;

import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.FormDeletedMessage;
import com.sougata.form_response_service.repository.FormResponseSummaryRepository;
import com.sougata.form_response_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component(MessagingChannelNames.FORM_DELETED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class FormDeletedMessageHandler implements MessageListener {

    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final FormResponseSummaryRepository formResponseSummaryRepository;
    private final RedissonClient redissonClient;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        var messageData = redisSerializer.deserialize(message.getBody(), FormDeletedMessage.class);

        formResponseSummaryRepository.deleteByFormId(messageData.getFormId());

        var redissonKeys = redissonClient.getKeys();

        redissonKeys.delete(
                CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_COUNT, messageData.getFormId()),
                CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARIES, messageData.getFormId())
        );

        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.IS_RESPONSE_ALREADY_SUBMITTED, "formId=" + messageData.getFormId()) + "::*"
        );
        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARY, "formId=" + messageData.getFormId()) + "::*"
        );
        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_SUMMARIES, "formId=" + messageData.getFormId()) + "::*"
        );
        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.INDIVIDUAL_FORM_RESPONSE, "formId=" + messageData.getFormId()) + "::*"
        );
        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.INDIVIDUAL_FORM_RESPONSE_BY_PAGE, "formId=" + messageData.getFormId()) + "::*"
        );
        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_BY_QUESTION, "formId=" + messageData.getFormId()) + "::*"
        );
    }
}
