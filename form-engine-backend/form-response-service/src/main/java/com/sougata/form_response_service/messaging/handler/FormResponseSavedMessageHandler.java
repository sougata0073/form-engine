package com.sougata.form_response_service.messaging.handler;

import com.sougata.form_engine.constant.DistributedLockNames;
import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.FormResponseSavedMessage;
import com.sougata.form_engine.dto.pgfunctionparameter.FormResponseQuestionIds;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.repository.FormResponseIndividualRepository;
import com.sougata.form_response_service.repository.FormResponseSummaryRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import com.sougata.form_response_service.service.responseManager.ResponseManagerFactory;
import com.sougata.form_response_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Component(MessagingChannelNames.FORM_RESPONSE_SAVED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class FormResponseSavedMessageHandler implements MessageListener {

    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final FormResponseSummaryRepository formResponseSummaryRepository;
    private final ResponseManagerFactory responseManagerFactory;
    private final FormResponseIndividualRepository formResponseIndividualRepository;
    private final RedissonClient redissonClient;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    @Transactional
    public void onMessage(Message message, byte @Nullable [] pattern) {

        var messageData = redisSerializer.deserialize(message.getBody(), FormResponseSavedMessage.class);

        var redisLock = redissonClient.getLock(DistributedLockNames.FORM_RESPONSE_PROCESS + messageData.getFormResponseId());

        if (!redisLock.tryLock()) {
            return;
        }

        if (formResponseIndividualRepository.existsById(messageData.getFormResponseId())) {
            redisLock.unlock();
            return;
        }

        var responseQuestionIds = messageData.getResponses()
                .stream()
                .map(QuestionResponsePutReqDto::getQuestionId)
                .collect(Collectors.toSet());

        var responseQuestionIdsJson = JsonUtil.toJson(
                new FormResponseQuestionIds(responseQuestionIds)
        );

        formResponseSummaryRepository.saveFormResponseSummary(
                messageData.getFormId(),
                messageData.getFormResponseId(),
                1L,
                1L,
                responseQuestionIdsJson
        );

        var responsesGroupedByQuestionType = messageData
                .getResponses()
                .stream()
                .collect(Collectors.groupingBy(QuestionResponsePutReqDto::getQuestionType));

        responsesGroupedByQuestionType.forEach((qType, responses) -> {
            var manager = responseManagerFactory.get(qType);

            manager.onResponseSave(messageData.getFormId(), messageData.getFormResponseId(), responses);
        });

        evictCache(messageData);

        redisLock.unlock();
    }

    private void evictCache(FormResponseSavedMessage messageData) {
        var formResponseCountCacheKey = CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_COUNT, messageData.getFormId());
        var responseSummariesCacheKey = CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARIES, messageData.getFormId());

        var responseByQuestionCacheKeyPattern = CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_BY_QUESTION, "formId=" + messageData.getFormId()) + "::*";
        var formResponseSummariesCacheKeyPattern = CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_SUMMARIES, "formId=" + messageData.getFormId()) + "::*";
        var responseSummaryCacheKeyPattern = CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARY, "formId=" + messageData.getFormId()) + "::*";

        var rKeys = redissonClient.getKeys();

        rKeys.delete(formResponseCountCacheKey, responseSummariesCacheKey);
        rKeys.deleteByPattern(responseByQuestionCacheKeyPattern);
        rKeys.deleteByPattern(formResponseSummariesCacheKeyPattern);
        rKeys.deleteByPattern(responseSummaryCacheKeyPattern);
    }
}
