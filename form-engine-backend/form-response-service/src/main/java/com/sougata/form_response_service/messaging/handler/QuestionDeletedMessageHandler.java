package com.sougata.form_response_service.messaging.handler;

import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryResDto;
import com.sougata.form_engine.dto.messaging.QuestionDeleteMessage;
import com.sougata.form_response_service.configuration.AppConfiguration;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import com.sougata.form_response_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component(MessagingChannelNames.QUESTION_DELETED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class QuestionDeletedMessageHandler implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;
    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;
    private final AppConfiguration appConfiguration;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {

        var messageData = redisSerializer.deserialize(message.getBody(), QuestionDeleteMessage.class);

        questionResponseSummaryRepository.deleteByQuestionId(messageData.getQuestionId());

        var responseSummariesCacheKey = CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARIES, messageData.getFormId());
        var cachedResponseSummaries = (ResponseSummaryResDto) redisTemplate.opsForValue().get(responseSummariesCacheKey);

        if (cachedResponseSummaries != null) {
            cachedResponseSummaries
                    .getResponses()
                    .removeIf(res ->
                            Objects.equals(res.getQuestionId(), messageData.getQuestionId())
                    );

            redisTemplate.opsForValue().set(
                    responseSummariesCacheKey,
                    cachedResponseSummaries,
                    Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes())
            );
        }

        var redissonKeys = redissonClient.getKeys();

        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_BY_QUESTION, "formId=" + messageData.getFormId(), "questionId=" + messageData.getQuestionId()) + "::*"
        );
        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_SUMMARIES, "formId=" + messageData.getFormId(), "questionId=" + messageData.getQuestionId()) + "::*"
        );
        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARY, "formId=" + messageData.getFormId(), "questionId=" + messageData.getQuestionId()) + "::*"
        );
        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.INDIVIDUAL_FORM_RESPONSE, "formId=" + messageData.getFormId()) + "::*"
        );
        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormResponseCacheNames.INDIVIDUAL_FORM_RESPONSE_BY_PAGE, "formId=" + messageData.getFormId()) + "::*"
        );
    }
}
