package com.sougata.form_response_service.messaging.handler;

import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.FormResponseSavedMessage;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import com.sougata.form_response_service.model.FormResponseIndividual;
import com.sougata.form_response_service.model.FormResponseSummary;
import com.sougata.form_response_service.repository.FormResponseIndividualRepository;
import com.sougata.form_response_service.repository.FormResponseSummaryRepository;
import com.sougata.form_response_service.service.responseManager.ResponseManagerFactory;
import com.sougata.form_response_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component(MessagingChannelNames.FORM_RESPONSE_SAVED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class FormResponseSavedMessageHandler implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final FormResponseSummaryRepository formResponseSummaryRepository;
    private final ResponseManagerFactory responseManagerFactory;
    private final FormResponseIndividualRepository formResponseIndividualRepository;

    @Override
    @Transactional
    public void onMessage(Message message, byte @Nullable [] pattern) {

        var messageData = redisSerializer.deserialize(message.getBody(), FormResponseSavedMessage.class);

        var formResponseSummaryOptional = formResponseSummaryRepository.findById(messageData.getFormId());

        FormResponseSummary formResponseSummary;

        if (formResponseSummaryOptional.isPresent()) {
            formResponseSummary = formResponseSummaryOptional.get();
            formResponseSummaryRepository.incrementResponseCount(messageData.getFormId(), 1L);
        } else {
            var formResponseSummaryToSave = new FormResponseSummary();

            formResponseSummaryToSave.setFormId(messageData.getFormId());
            formResponseSummaryToSave.setResponseCount(1L);

            formResponseSummary = formResponseSummaryRepository.save(formResponseSummaryToSave);
        }

        var formResponseIndividual = formResponseIndividualRepository.findById(messageData.getFormResponseId())
                .orElseGet(() -> {
                    var formResponseIndividualToSave = new FormResponseIndividual();

                    formResponseIndividualToSave.setFormResponseId(messageData.getFormResponseId());

                    return formResponseIndividualRepository.save(formResponseIndividualToSave);
                });

        var responsesGroupedByQuestionType = messageData
                .getResponses()
                .stream()
                .collect(Collectors.groupingBy(QuestionResponsePutReqDto::getQuestionType));

        responsesGroupedByQuestionType.forEach((qType, responses) -> {
            var manager = responseManagerFactory.get(qType);

            manager.onResponseSave(formResponseSummary, formResponseIndividual, responses);
        });

        var formResponseCountCacheKey = CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_COUNT, messageData.getFormId());
        var responseSummariesCacheKey = CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARIES, messageData.getFormId());

        var responseByQuestionCacheKeys = redisTemplate.keys(CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_BY_QUESTION, "formId=" + messageData.getFormId()) + "::*");
        var formResponseSummariesCacheKeys = redisTemplate.keys(CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_SUMMARIES, "formId=" + messageData.getFormId()) + "::*");
        var responseSummaryCacheKeys = redisTemplate.keys(CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARY, "formId=" + messageData.getFormId()) + "::*");

        var cacheKeys = new ArrayList<>(
                List.of(formResponseCountCacheKey, responseSummariesCacheKey)
        );

        cacheKeys.addAll(responseByQuestionCacheKeys);
        cacheKeys.addAll(formResponseSummariesCacheKeys);
        cacheKeys.addAll(responseSummaryCacheKeys);

        redisTemplate.delete(cacheKeys);
    }
}
