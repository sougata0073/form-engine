package com.sougata.form_response_service.scheduledtask;

import com.sougata.form_engine.constant.RedisConsumerGroupNames;
import com.sougata.form_engine.constant.RedisStreamKeys;
import com.sougata.form_engine.constant.RedisStreamNames;
import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_engine.dto.messaging.FormResponseSavedMessage;
import com.sougata.form_engine.dto.pgfunctionparameter.FormResponseCounts;
import com.sougata.form_engine.dto.pgfunctionparameter.FormResponseInfos;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseCounts;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputreqbatch.QuestionResponseBatch;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.repository.FormResponseSummaryRepository;
import com.sougata.form_response_service.service.responseManager.ResponseManagerFactory;
import com.sougata.form_response_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProcessFormResponseTask {

    private final ResponseManagerFactory responseManagerFactory;
    private final RedisTemplate<String, Object> redisTemplate;
    private final FormResponseSummaryRepository formResponseSummaryRepository;
    private final RedissonClient redissonClient;

    @SuppressWarnings("unchecked")
    @Transactional
    @Scheduled(fixedDelay = 1, initialDelay = 2, timeUnit = TimeUnit.SECONDS)
    public void processFormResponses() {
        var messages = redisTemplate.opsForStream().read(
                Consumer.from(RedisConsumerGroupNames.FORM_RESPONSE_CONSUMER, "worker-1"),
                StreamReadOptions.empty().count(1000),
                StreamOffset.create(RedisStreamNames.FORM_RESPONSE_STREAM, ReadOffset.lastConsumed())
        );

        if (messages.isEmpty()) {
            return;
        }

        var messageIdsToAcknowledge = new ArrayList<RecordId>();
        var formResponseSavedMessages = new ArrayList<FormResponseSavedMessage>();

        messages.forEach(message -> {
            var messageId = message.getId();

            if (message.getValue().get(RedisStreamKeys.FORM_RESPONSE) instanceof FormResponseSavedMessage formResponseSavedMessage) {
                formResponseSavedMessages.add(formResponseSavedMessage);
            }

            messageIdsToAcknowledge.add(messageId);

        });

        var formResponseCounts = new FormResponseCounts();
        var formResponseCountList = new ArrayList<FormResponseCounts.FormIdResponseCount>();

        var questionResponseCounts = new QuestionResponseCounts();
        var questionResponseCountList = new ArrayList<QuestionResponseCounts.QuestionResponseCount>();

        var formResponseInfos = new FormResponseInfos();
        var formResponseInfoList = new ArrayList<FormResponseInfos.FormResponseInfo>();

        var questionResponseBatchList = new ArrayList<QuestionResponseBatch<QuestionResponseBatch.Response>>();

        formResponseSavedMessages
                .stream()
                .collect(Collectors.groupingBy(FormResponseSavedMessage::getFormId))
                .forEach((formId, formResponses) -> {

                    formResponseCountList.add(
                            new FormResponseCounts.FormIdResponseCount(
                                    formId,
                                    (long) formResponses.size()
                            )
                    );

                    formResponseInfoList.addAll(
                            formResponses
                                    .stream()
                                    .map(formResponse ->
                                            new FormResponseInfos.FormResponseInfo(
                                                    formResponse.getFormResponseId(),
                                                    formId,
                                                    formResponse.getResponderId()
                                            )
                                    )
                                    .toList()
                    );

                    formResponses
                            .stream()
                            .flatMap(formResponse ->
                                    formResponse.getResponses()
                                            .stream()
                                            .map(qr ->
                                                    new FormResponseInfoQuestionResponse<>(
                                                            formResponse.getFormResponseId(),
                                                            formResponse.getResponderId(),
                                                            qr
                                                    )
                                            )
                            )
                            .collect(
                                    Collectors.groupingBy(questionResponseManagerBatchInput ->
                                            questionResponseManagerBatchInput.getQuestionResponsePutReq().getQuestionId()
                                    )
                            )
                            .forEach((questionId, questionResponseManagerBatchInputs) -> {

                                var firstQuestionType = questionResponseManagerBatchInputs
                                        .stream()
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException(
                                                "Found empty question response put request list found for question ID: " + questionId
                                        ))
                                        .getQuestionResponsePutReq()
                                        .getQuestionType();

                                var areAllQuestionTypeSame = questionResponseManagerBatchInputs
                                        .stream()
                                        .allMatch(q ->
                                                firstQuestionType == q.getQuestionResponsePutReq().getQuestionType()
                                        );

                                if (!areAllQuestionTypeSame) {
                                    throw new RuntimeException("All question types are not same for question ID: " + questionId);
                                }

                                var manager = responseManagerFactory.get(firstQuestionType);

                                var questionResponseBatch = manager.mapToBatchResponse(questionResponseManagerBatchInputs);

                                questionResponseBatch.setQuestionId(questionId);
                                questionResponseBatch.setQuestionType(firstQuestionType);

                                questionResponseCountList.add(
                                        new QuestionResponseCounts.QuestionResponseCount(
                                                questionResponseBatch.getQuestionId(),
                                                (long) questionResponseManagerBatchInputs.size(),
                                                formId
                                        )
                                );

                                questionResponseBatchList.add(questionResponseBatch);

                            });
                });

        formResponseCounts.setCounts(formResponseCountList);
        questionResponseCounts.setCounts(questionResponseCountList);
        formResponseInfos.setFormResponseInfos(formResponseInfoList);

        formResponseSummaryRepository.saveFormResponseSummary(
                JsonUtil.toJson(formResponseCounts),
                JsonUtil.toJson(questionResponseCounts),
                JsonUtil.toJson(formResponseInfos)
        );

        questionResponseBatchList
                .stream()
                .collect(Collectors.groupingBy(QuestionResponseBatch::getQuestionType))
                .forEach((questionType, questionResponseBatches) -> {
                    var manager = responseManagerFactory.get(questionType);

                    manager.saveBatched(questionResponseBatches);
                });

        redisTemplate.opsForStream().acknowledge(
                RedisStreamNames.FORM_RESPONSE_STREAM,
                RedisConsumerGroupNames.FORM_RESPONSE_CONSUMER,
                messageIdsToAcknowledge.toArray(new RecordId[0])
        );

        evictCache(formResponseSavedMessages);
    }

    private void evictCache(List<FormResponseSavedMessage> formResponseSavedMessages) {

        var cacheKeys = new ArrayList<String>();
        var cacheKeyPatterns = new ArrayList<String>();

        formResponseSavedMessages.forEach(formResponseSavedMessage -> {
            cacheKeys.addAll(
                    List.of(
                            CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_COUNT, formResponseSavedMessage.getFormId()),
                            CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARIES, formResponseSavedMessage.getFormId())
                    )
            );
            cacheKeyPatterns.addAll(
                    List.of(
                            CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_BY_QUESTION, "formId=" + formResponseSavedMessage.getFormId()) + "::*",
                            CacheUtil.buildKey(FormResponseCacheNames.FORM_RESPONSE_SUMMARIES, "formId=" + formResponseSavedMessage.getFormId()) + "::*",
                            CacheUtil.buildKey(FormResponseCacheNames.RESPONSE_SUMMARY, "formId=" + formResponseSavedMessage.getFormId()) + "::*"
                    )
            );
        });

        var rKeys = redissonClient.getKeys();

        rKeys.deleteAsync(cacheKeys.toArray(new String[0]));
        cacheKeyPatterns.forEach(rKeys::deleteByPatternAsync);
    }

}
