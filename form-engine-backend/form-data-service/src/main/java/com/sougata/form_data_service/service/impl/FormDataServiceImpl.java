package com.sougata.form_data_service.service.impl;

import com.sougata.form_data_service.configuration.AppConfiguration;
import com.sougata.form_data_service.exception.FormSubmitException;
import com.sougata.form_data_service.feignClient.AuthServiceFeignClient;
import com.sougata.form_data_service.feignClient.FormResponseServiceFeignClient;
import com.sougata.form_data_service.feignClient.FormServiceFeignClient;
import com.sougata.form_data_service.formValidation.service.FormSchemaService;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.FormResponseUser;
import com.sougata.form_data_service.repository.FormResponseRepository;
import com.sougata.form_data_service.repository.FormResponseUserRepository;
import com.sougata.form_data_service.service.FormDataService;
import com.sougata.form_data_service.service.FormDataServiceCached;
import com.sougata.form_data_service.service.responseManager.ResponseManagerFactory;
import com.sougata.form_data_service.util.CacheUtil;
import com.sougata.form_engine.constant.*;
import com.sougata.form_engine.constant.cache.CommonCacheNames;
import com.sougata.form_engine.constant.cache.FormDataCacheNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.form.FormResponsePutReqDto;
import com.sougata.form_engine.dto.form.FormResponsePutResDto;
import com.sougata.form_engine.dto.messaging.FormResponseDeleteMessage;
import com.sougata.form_engine.dto.messaging.FormResponseSavedMessage;
import com.sougata.form_engine.dto.others.IsResponseSubmittedDto;
import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import com.sougata.form_engine.dto.validation.request.ResponseValidationRequestDto;
import com.sougata.form_engine.exception.FormDetailsMayChangedException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormDataServiceImpl implements FormDataService {

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
    private final FormDataServiceCached formDataServiceCached;
    private final AuthServiceFeignClient authServiceFeignClient;

    @Override
    public FormResponsePutResDto saveFormResponse(final UUID formId, final FormResponsePutReqDto req, final UUID responderId, final String clientIps) {

        var formDetails = formServiceFeignClient.getFormDetails(formId);

        if (!Objects.equals(formDetails, req.getFormDetailsResponseSubmittedOn())) {
            throw new FormDetailsMayChangedException();
        }

        if (formDetails.getFormInfo().getGeneralSettings().getResponderNeedsToBeAuthenticated() && responderId == null) {
            throw new RuntimeException("User ID is required for verified email collection.");
        }

        if (formDetails.getFormInfo().getResponseSettings().getEmailCollectionOption() == FormEmailCollectionOption.RESPONDER_INPUT && req.getEmail() == null) {
            throw new RuntimeException("Email is required for responder input email collection.");
        }

        if (!formDetails.getFormInfo().getGeneralSettings().getPublished()) {
            throw new FormSubmitException("This form is not published yet.");
        }

        if (!formDetails.getFormInfo().getGeneralSettings().getAcceptingResponse()) {
            throw new FormSubmitException("This form is not accepting response.");
        }

        boolean isAcceptingDateExceeded =
                formDetails.getFormInfo().getGeneralSettings().getStopAcceptingResponseOn() != null &&
                        Instant.now().isAfter(formDetails.getFormInfo().getGeneralSettings().getStopAcceptingResponseOn());

        if (isAcceptingDateExceeded) {
            throw new FormSubmitException("This form is no longer accepting responses.");
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

        var responseLimit = formDetails.getFormInfo().getGeneralSettings().getResponseLimit() == null
                ? FormConstant.MAX_FORM_RESPONSE_LIMIT
                : formDetails.getFormInfo().getGeneralSettings().getResponseLimit();

        if (incrementedFormResponseCount > responseLimit) {

            stringRedisTemplate.opsForValue().decrement(formResponseCountRedisKey);

            throw new FormSubmitException("This form has reached its maximum response limit and is no longer accepting responses.");
        }

        var formResponseUserRedisLockKey = DistributedLockNames.formMultipleResponse(
                formId, responderId == null ? clientIps : responderId.toString()
        );
        var formResponseUserRedisLock = redissonClient.getLock(formResponseUserRedisLockKey);

        try {

            var validationBody = new ResponseValidationRequestDto(req.getResponses());

            var validationResponse = formSchemaService.validateResponse(formId, formDetails, validationBody);

            formResponseUserRedisLock.lock();

            if (formDetails.getFormInfo().getResponseSettings().getSingleResponseOnly()
                    && formDataServiceCached.getIsResponseAlreadySubmitted(formId, responderId == null ? clientIps : responderId.toString()).getIsSubmitted()
            ) {
                throw new FormSubmitException("You have already submitted a response for this form.");
            }

            var formResponsePartitionKey = new FormResponse.PartitionKey();
            formResponsePartitionKey.setFormId(formId);

            var formResponse = new FormResponse();
            formResponse.setKey(formResponsePartitionKey);
            formResponse.setResponderId(responderId == null ? clientIps : responderId.toString());
            formResponse.setRespondedQuestionMap(
                    req.getResponses()
                            .stream()
                            .collect(Collectors.toMap(QuestionResponsePutReqDto::getQuestionId, QuestionResponsePutReqDto::getQuestionType))
            );

            if (formDetails.getFormInfo().getResponseSettings().getEmailCollectionOption() == FormEmailCollectionOption.RESPONDER_INPUT) {
                formResponse.setResponderEmail(req.getEmail());
            } else if (formDetails.getFormInfo().getResponseSettings().getEmailCollectionOption() == FormEmailCollectionOption.VERIFIED) {
                formResponse.setResponderEmail(authServiceFeignClient.getUserSummary(responderId).getEmail());
            }

            var savedFormResponse = formResponseRepository.save(formResponse);

            var formResponseUserPartitionKey = new FormResponseUser.PartitionKey();
            formResponseUserPartitionKey.setFormId(formId);
            formResponseUserPartitionKey.setResponderId(responderId == null ? clientIps : responderId.toString());

            var formResponseUser = new FormResponseUser(formResponseUserPartitionKey);
            var savedFormResponseUser = formResponseUserRepository.save(formResponseUser);

            var questionResponseFutures = new ArrayList<CompletableFuture<Void>>();

            req.getResponses().forEach(response -> {
                var responseManager = responseManagerFactory.get(
                        response.getQuestionType()
                );
                var future = responseManager.create(
                        savedFormResponse.getKey().getFormId(),
                        savedFormResponse.getKey().getFormResponseId(),
                        response
                );

                questionResponseFutures.add(future);
            });

            CompletableFuture.allOf(questionResponseFutures.toArray(CompletableFuture[]::new)).join();

            redisTemplate.opsForStream().add(
                    StreamRecords.newRecord()
                            .ofMap(Map.of(RedisStreamKeys.FORM_RESPONSE, new FormResponseSavedMessage(
                                    formId,
                                    savedFormResponse.getKey().getFormResponseId(),
                                    savedFormResponse.getResponderId(),
                                    savedFormResponse.getResponderEmail(),
                                    req.getResponses()
                            )))
                            .withStreamKey(RedisStreamNames.FORM_RESPONSE_STREAM)
            );

            var isResponseAlreadySubmittedCacheKey = CacheUtil.buildKey(
                    FormDataCacheNames.IS_RESPONSE_ALREADY_SUBMITTED,
                    "formId=" + formId,
                    "userId=" + savedFormResponse.getResponderId()
            );

            redisTemplate.opsForValue().set(
                    isResponseAlreadySubmittedCacheKey,
                    new IsResponseSubmittedDto(true),
                    Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes())
            );

            return new FormResponsePutResDto(savedFormResponse.getKey().getFormResponseId());

        } catch (Exception ex) {

            stringRedisTemplate.opsForValue().decrement(formResponseCountRedisKey);
            throw ex;

        } finally {

            if (formResponseUserRedisLock.isHeldByCurrentThread()) {
                formResponseUserRedisLock.unlock();
            }

        }
    }

    @Override
    public SuccessMessageDto deleteFormResponse(UUID formId, UUID formResponseId) {
        var formResponses = formDataServiceCached.getFormResponse(formId, formResponseId);

        var savedFormResponse = formResponseRepository.findById(new FormResponse.PartitionKey(formId, formResponseId))
                .orElseThrow(
                        () -> new RuntimeException("Form response not found with form ID: " + formId + " and form response ID: " + formResponseId)
                );

        formResponseRepository.delete(savedFormResponse);

        var questionResponseDeleteFutures = new ArrayList<CompletableFuture<Void>>();

        savedFormResponse.getRespondedQuestionMap()
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue))
                .forEach(((questionType, entries) -> {
                    var questionIds = entries.stream().map(Map.Entry::getKey).collect(Collectors.toSet());
                    var manager = responseManagerFactory.get(questionType);

                    var future = manager.deleteAllByQuestionIds(formId, formResponseId, questionIds);

                    questionResponseDeleteFutures.add(future);
                }));

        CompletableFuture.allOf(questionResponseDeleteFutures.toArray(new CompletableFuture[0])).join();

        redisTemplate.convertAndSend(
                MessagingChannelNames.FORM_RESPONSE_DELETED,
                new FormResponseDeleteMessage(formId, formResponseId, savedFormResponse.getResponderId(), formResponses.getResponses())
        );

        return SuccessMessageDto.create("Response deleted successfully. Form id: " + formId + " Form response ID: " + formResponseId);
    }
}
