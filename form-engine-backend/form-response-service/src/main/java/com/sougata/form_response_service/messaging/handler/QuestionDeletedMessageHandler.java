package com.sougata.form_response_service.messaging.handler;

import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.QuestionDeleteMessage;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import com.sougata.form_response_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(MessagingChannelNames.QUESTION_DELETED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class QuestionDeletedMessageHandler implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {

        var messageData = redisSerializer.deserialize(message.getBody(), QuestionDeleteMessage.class);

        questionResponseSummaryRepository.deleteByQuestionId(messageData.getQuestionId());

        var formResponseCountCacheKey = CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_COUNT, messageData.getFormId());
        var responseSummariesCacheKey = CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARIES, messageData.getFormId());

        var responseByQuestionCacheKeys = redisTemplate.keys(CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_BY_QUESTION, "formId=" + messageData.getFormId()) + "::*");
        var formResponseSummariesCacheKeys = redisTemplate.keys(CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_SUMMARIES, "formId=" + messageData.getFormId()) + "::*");
        var responseSummaryCacheKeys = redisTemplate.keys(CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARY, "formId=" + messageData.getFormId()) + "::*");
        var individualFormResponseCacheKeys = redisTemplate.keys(CacheUtil.buildKey(FormResponseCacheNames.INDIVIDUAL_FORM_RESPONSE, "formId=" + messageData.getFormId()) + "::*");

        var cacheKeys = new ArrayList<>(
                List.of(
                        formResponseCountCacheKey,
                        responseSummariesCacheKey
                )
        );
        cacheKeys.addAll(responseByQuestionCacheKeys);
        cacheKeys.addAll(formResponseSummariesCacheKeys);
        cacheKeys.addAll(responseSummaryCacheKeys);
        cacheKeys.addAll(individualFormResponseCacheKeys);

        redisTemplate.delete(cacheKeys);
    }
}
