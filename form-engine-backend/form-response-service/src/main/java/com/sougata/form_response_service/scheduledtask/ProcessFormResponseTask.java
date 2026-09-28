package com.sougata.form_response_service.scheduledtask;

import com.sougata.form_engine.constant.RedisConsumerGroupNames;
import com.sougata.form_engine.constant.RedisStreamKeys;
import com.sougata.form_engine.constant.RedisStreamNames;
import com.sougata.form_engine.dto.form.FormResponseBatch;
import com.sougata.form_engine.dto.messaging.FormResponseSavedMessage;
import com.sougata.form_engine.dto.question.responseputreqbatch.QuestionResponseManagerBatchInput;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.service.responseManager.ResponseManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProcessFormResponseTask {

    private final ResponseManagerFactory responseManagerFactory;
    private final RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
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
        var formResponses = new ArrayList<FormResponseSavedMessage>();

        messages.forEach(message -> {
            var messageId = message.getId();

            if (message.getValue().get(RedisStreamKeys.FORM_RESPONSE) instanceof FormResponseSavedMessage formResponseSavedMessage) {
                formResponses.add(formResponseSavedMessage);
            }

            messageIdsToAcknowledge.add(messageId);

        });

        var formResponseBatched = getBatchedFormResponses(formResponses);

        System.out.println(JsonUtil.toJson(formResponseBatched));

        redisTemplate.opsForStream().acknowledge(
                RedisStreamNames.FORM_RESPONSE_STREAM,
                RedisConsumerGroupNames.FORM_RESPONSE_CONSUMER,
                messageIdsToAcknowledge.toArray(new RecordId[0])
        );
    }

    // So many streams
    private FormResponseBatch getBatchedFormResponses(List<FormResponseSavedMessage> formResponses) {

        var formResponseBatch = new FormResponseBatch();

        var requests = formResponses
                .stream()
                .collect(Collectors.groupingBy(FormResponseSavedMessage::getFormId))
                .entrySet()
                .stream()
                .map(entryFormResponse -> {
                    var reqPerForm = new FormResponseBatch.RequestPerForm();

                    reqPerForm.setFormId(entryFormResponse.getKey());

                    var responses = entryFormResponse.getValue()
                            .stream()
                            .flatMap(formResponse ->
                                    formResponse.getResponses()
                                            .stream()
                                            .map(qr ->
                                                    new QuestionResponseManagerBatchInput<>(formResponse.getFormResponseId(), qr)
                                            )
                            )
                            .collect(
                                    Collectors.groupingBy(questionResponseManagerBatchInput ->
                                            questionResponseManagerBatchInput.getQuestionResponsePutReq().getQuestionId()
                                    )
                            )
                            .entrySet()
                            .stream()
                            .map(entryQResPutReq -> {
                                var firstQuestionType = entryQResPutReq
                                        .getValue()
                                        .stream()
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException(
                                                "Found empty question response put request list found for question ID: " + entryQResPutReq.getKey()
                                        ))
                                        .getQuestionResponsePutReq()
                                        .getQuestionType();

                                var areAllQuestionTypeSame = entryQResPutReq
                                        .getValue()
                                        .stream()
                                        .allMatch(q ->
                                                firstQuestionType == q.getQuestionResponsePutReq().getQuestionType()
                                        );

                                if (!areAllQuestionTypeSame) {
                                    throw new RuntimeException("All question types are not same for question ID: " + entryQResPutReq.getKey());
                                }

                                var manager = responseManagerFactory.get(firstQuestionType);

                                return manager.mapToBatchResponse(entryQResPutReq.getKey(), entryQResPutReq.getValue());
                            }).toList();

                    reqPerForm.setResponses(responses);

                    return reqPerForm;
                }).toList();

        formResponseBatch.setRequests(requests);

        return formResponseBatch;
    }

}
