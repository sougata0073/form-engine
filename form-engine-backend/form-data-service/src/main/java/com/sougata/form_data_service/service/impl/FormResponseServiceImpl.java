package com.sougata.form_data_service.service.impl;

import com.sougata.form_data_service.configuration.AppConfiguration;
import com.sougata.form_data_service.exception.FormSubmitException;
import com.sougata.form_data_service.feignClient.FormResponseServiceFeignClient;
import com.sougata.form_data_service.feignClient.FormServiceFeignClient;
import com.sougata.form_data_service.formValidation.service.FormSchemaService;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.FormResponseUser;
import com.sougata.form_data_service.repository.FormResponseRepository;
import com.sougata.form_data_service.repository.FormResponseUserRepository;
import com.sougata.form_data_service.service.FormResponseService;
import com.sougata.form_data_service.service.responseManager.ResponseManagerFactory;
import com.sougata.form_engine.constant.DistributedLockNames;
import com.sougata.form_engine.constant.RedisStreamKeys;
import com.sougata.form_engine.constant.RedisStreamNames;
import com.sougata.form_engine.constant.cache.CommonCacheNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.form.FormResponsePutReqDto;
import com.sougata.form_engine.dto.form.FormResponsePutResDto;
import com.sougata.form_engine.dto.formResponse.individual.FormResponseIndividualDtoNoPage;
import com.sougata.form_engine.dto.formResponse.individual.QuestionResponseIndividualDto;
import com.sougata.form_engine.dto.messaging.FormResponseDeleteMessage;
import com.sougata.form_engine.dto.messaging.FormResponseSavedMessage;
import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import com.sougata.form_engine.dto.validation.request.ResponseValidationRequestDto;
import com.sougata.form_engine.exception.FormDetailsMayChangedException;
import com.sougata.form_engine.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormResponseServiceImpl implements FormResponseService {

    private final FormResponseRepository formResponseRepository;
    private final FormResponseUserRepository formResponseUserRepository;
    private final ResponseManagerFactory responseManagerFactory;
    private final FormServiceFeignClient formServiceFeignClient;
    private final FormSchemaService formSchemaService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final FormResponseServiceFeignClient formResponseServiceFeignClient;
    private final AppConfiguration appConfiguration;
    private final RedissonClient redissonClient;

    @Override
    public FormResponsePutResDto saveResponse(UUID formId, FormResponsePutReqDto req, UUID responderId) {

        var formDetails = formServiceFeignClient.getFormDetails(formId);

        if (!formDetails.getPublished()) {
            throw new FormSubmitException("This form is not published yet.");
        }

        if (!formDetails.getAcceptingResponse()) {
            throw new FormSubmitException("This form is not accepting response.");
        }

        boolean isAcceptingDateExceeded =
                formDetails.getStopAcceptingResponseOn() != null &&
                        Instant.now().isAfter(formDetails.getStopAcceptingResponseOn());

        if (isAcceptingDateExceeded) {
            throw new FormSubmitException("This form is no longer accepting responses.");
        }

//        TODO: Not working
//        if (!formDetails.equals(req.getFormDetailsResponseSubmittedOn())) {
//            throw new FormDetailsMayChangedException();
//        }

        if (!JsonUtil.toJson(formDetails).equals(JsonUtil.toJson(req.getFormDetailsResponseSubmittedOn()))) {
            throw new FormDetailsMayChangedException();
        }

        var formResponseCountRedisKey = CommonCacheNames.FORM_RESPONSE_COUNT + "::" + formId;

        if (!stringRedisTemplate.hasKey(formResponseCountRedisKey)) {
            var formResponseCount = formResponseServiceFeignClient.getFormResponseCount(formId);

            stringRedisTemplate.opsForValue().setIfAbsent(
                    formResponseCountRedisKey,
                    formResponseCount.getCount().toString(),
                    Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes())
            );
        }

        var incrementedFormResponseCount = stringRedisTemplate.opsForValue().increment(formResponseCountRedisKey);

        if (formDetails.getStopAcceptingResponseAfterResponse() != null
                && incrementedFormResponseCount > formDetails.getStopAcceptingResponseAfterResponse()) {

            stringRedisTemplate.opsForValue().decrement(formResponseCountRedisKey);

            throw new FormSubmitException("This form has reached its maximum response limit and is no longer accepting responses.");
        }

        var formResponseUserRedisLockKey = DistributedLockNames.FORM_RESPONSE_USER + formId + CommonCacheNames.SEPARATOR + responderId;
        var formResponseUserRedisLock = redissonClient.getLock(formResponseUserRedisLockKey);

        try {

            var validationBody = new ResponseValidationRequestDto(req.getResponses());

            var validationResponse = formSchemaService.validateResponse(formId, formDetails, validationBody);

            formResponseUserRedisLock.lock();

            var formResponseUserPartitionKey = new FormResponseUser.PartitionKey();
            formResponseUserPartitionKey.setFormId(formId);
            formResponseUserPartitionKey.setUserId(responderId);

            if (formResponseUserRepository.existsById(formResponseUserPartitionKey)) {
                throw new FormSubmitException("You have already submitted a response for this form.");
            }

            var formResponsePartitionKey = new FormResponse.PartitionKey();
            formResponsePartitionKey.setFormId(formId);

            var formResponse = new FormResponse();
            formResponse.setKey(formResponsePartitionKey);
            formResponse.setUserId(responderId);
            formResponse.setRespondedQuestionMap(
                    req.getResponses()
                            .stream()
                            .collect(Collectors.toMap(QuestionResponsePutReqDto::getQuestionId, QuestionResponsePutReqDto::getQuestionType))
            );

            var savedFormResponse = formResponseRepository.save(formResponse);

            var formResponseUser = new FormResponseUser(formResponseUserPartitionKey);
            var savedFormResponseUser = formResponseUserRepository.save(formResponseUser);

            var questionResponseFutures = new ArrayList<CompletableFuture<Void>>();

            req.getResponses().forEach(response -> {
                var responseManager = responseManagerFactory.get(
                        response.getQuestionType()
                );
                var future = responseManager.create(response, savedFormResponse.getKey().getFormResponseId());

                questionResponseFutures.add(future);
            });

            CompletableFuture.allOf(questionResponseFutures.toArray(CompletableFuture[]::new)).join();

            redisTemplate.opsForStream().add(
                    StreamRecords.newRecord()
                            .ofMap(Map.of(RedisStreamKeys.FORM_RESPONSE, new FormResponseSavedMessage(
                                    formId,
                                    savedFormResponse.getKey().getFormResponseId(),
                                    responderId,
                                    req.getResponses()
                            )))
                            .withStreamKey(RedisStreamNames.FORM_RESPONSE_STREAM)
            );

            return new FormResponsePutResDto(savedFormResponse.getKey().getFormResponseId());

        } catch (Exception ex) {

            stringRedisTemplate.opsForValue().decrement(formResponseCountRedisKey);
            throw ex;

        } finally {

            formResponseUserRedisLock.unlock();

        }
    }

    @Override
    public FormResponseIndividualDtoNoPage getFormResponse(UUID formId, UUID formResponseId) {
        var formResponseKey = new FormResponse.PartitionKey(formId, formResponseId);

        var formResponse = formResponseRepository.findById(formResponseKey)
                .orElseThrow(
                        () -> new RuntimeException("Form response not found for form Id: " + formId + " and form response ID: " + formResponseId)
                );

        var responseFutures = new ArrayList<CompletableFuture<List<QuestionResponseIndividualDto>>>();

        Optional.ofNullable(formResponse.getRespondedQuestionMap())
                .orElse(Collections.emptyMap())
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue))
                .forEach(((questionType, entries) -> {
                    var questionIds = entries.stream().map(Map.Entry::getKey).toList();
                    var manager = responseManagerFactory.get(questionType);

                    responseFutures.add(manager.getFormResponses(formResponseId, questionIds));
                }));

        var responses = CompletableFuture.allOf(responseFutures.toArray(CompletableFuture[]::new))
                .thenApply(v -> responseFutures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .toList()
                ).join();

        return new FormResponseIndividualDtoNoPage(
                formResponse.getKey().getFormResponseId(),
                formResponse.getUserId(),
                responses
        );
    }

    @Override
    public SuccessMessageDto deleteFormResponse(UUID formId, UUID formResponseId) {
        var formResponse = formResponseRepository.findById(new FormResponse.PartitionKey(formId, formResponseId))
                .orElseThrow(
                        () -> new RuntimeException("Form response not found with form ID: " + formId + " and form response ID: " + formResponseId)
                );

        formResponseRepository.delete(formResponse);

        var questionResponseDeleteFutures = new ArrayList<CompletableFuture<Void>>();

        formResponse.getRespondedQuestionMap()
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue))
                .forEach(((questionType, entries) -> {
                    var questionIds = entries.stream().map(Map.Entry::getKey).toList();
                    var manager = responseManagerFactory.get(questionType);

                    var future = manager.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

                    questionResponseDeleteFutures.add(future);
                }));

        CompletableFuture.allOf(questionResponseDeleteFutures.toArray(new CompletableFuture[0])).join();

        redisTemplate.convertAndSend(
                MessagingChannelNames.FORM_RESPONSE_DELETED,
                new FormResponseDeleteMessage(formId, formResponseId, formResponse.getUserId())
        );

        return SuccessMessageDto.create("Response deleted successfully. Form id: " + formId + " Form response ID: " + formResponseId);
    }
}
