package com.sougata.form_response_service.messaging.handler;

import com.sougata.form_engine.constant.Messaging;
import com.sougata.form_engine.constant.MessagingChannelNames;
import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_engine.dto.messaging.FormResponseDeleteMessage;
import com.sougata.form_engine.dto.messaging.FormResponseSavedMessage;
import com.sougata.form_response_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component(MessagingChannelNames.FORM_RESPONSE_SAVED + "_" + Messaging.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class FormResponseSavedMessageHandler implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        var messageData = objectMapper.readValue(
                new String(message.getBody(), StandardCharsets.UTF_8), FormResponseSavedMessage.class
        );

        var formResponseCountCacheKey = CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_COUNT, messageData.getFormId());
        var responseSummariesCacheKey = CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARIES, messageData.getFormId());

        var responseByQuestionCacheKeys = redisTemplate.keys(CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_BY_QUESTION, "formId=" + messageData.getFormId()) + "::*");
        var formResponseSummariesCacheKeys = redisTemplate.keys(CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_SUMMARIES, "formId=" + messageData.getFormId()) + "::*");
        var responseSummaryCacheKeys = redisTemplate.keys(CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARY, "formId=" + messageData.getFormId()) + "::*");

        var cacheKeys = new ArrayList<String>(
                List.of(formResponseCountCacheKey, responseSummariesCacheKey)
        );

        cacheKeys.addAll(responseByQuestionCacheKeys);
        cacheKeys.addAll(formResponseSummariesCacheKeys);
        cacheKeys.addAll(responseSummaryCacheKeys);

        redisTemplate.delete(cacheKeys);
    }
}
