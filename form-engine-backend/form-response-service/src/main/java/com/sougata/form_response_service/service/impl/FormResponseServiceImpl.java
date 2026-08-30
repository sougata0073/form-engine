package com.sougata.form_response_service.service.impl;

import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_engine.dto.form.FormResponseCountDto;
import com.sougata.form_engine.dto.form.FormResponseSummariesDto;
import com.sougata.form_engine.dto.form.FormResponseSummaryDto;
import com.sougata.form_engine.dto.formResponse.individual.ResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.individual.ResponseIndividualResDto;
import com.sougata.form_engine.dto.formResponse.question.ResponseByQuestionResponse;
import com.sougata.form_engine.dto.formResponse.question.ResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryResDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.user.UserSummaryShortDto;
import com.sougata.form_response_service.configuration.AppConfiguration;
import com.sougata.form_response_service.feignClient.AuthServiceFeignClient;
import com.sougata.form_response_service.feignClient.FormServiceFeignClient;
import com.sougata.form_response_service.repository.FormResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseRepository;
import com.sougata.form_response_service.service.FormResponseService;
import com.sougata.form_response_service.service.responseManager.ResponseManagerFactory;
import com.sougata.form_response_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormResponseServiceImpl implements FormResponseService {

    private final FormResponseRepository formResponseRepository;
    private final FormServiceFeignClient formServiceFeignClient;
    private final AuthServiceFeignClient authServiceFeignClient;
    private final ResponseManagerFactory responseManagerFactory;
    private final QuestionResponseRepository questionResponseRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AppConfiguration appConfiguration;

    @Override
    public boolean getIsResponseAlreadySubmitted(UUID formId, UUID userId) {
        return formResponseRepository.existsByFormIdAndUserId(formId, userId);
    }

    @Override
    public ResponseSummaryResDto getResponseSummaries(UUID formId) {
        var questions = formServiceFeignClient.getFormDetails(formId).getQuestions();

        var questionTypeMap = questions.stream().collect(Collectors.groupingBy(QuestionDetailsDto::getQuestionType));

        var result = new ArrayList<ResponseSummaryDto<?>>();

        questionTypeMap.keySet().forEach(qType -> {
            var filteredQuestions = questionTypeMap.get(qType);

            var manager = responseManagerFactory.get(qType);
            var summaries = manager.getResponseSummaries(formId, filteredQuestions);

            result.addAll(summaries);
        });

        result.sort(Comparator.comparingInt(ResponseSummaryDto::getOrderIndex));

        return new ResponseSummaryResDto(result);
    }

    @Override
    public ResponseSummaryDto<?> getResponseSummary(UUID formId, Long questionId, Pageable pageable) {
        var question = formServiceFeignClient.getQuestion(formId, questionId);
        var manager = responseManagerFactory.get(question.getQuestionType());

        return manager.getResponseSummary(formId, questionId, question, pageable);
    }

    @Override
    public FormResponseSummariesDto getFormResponseSummaries(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
        var questionSummary = formServiceFeignClient.getQuestionSummary(formId, questionId);
        var manager = responseManagerFactory.get(questionSummary.getQuestionType());

        var resAndUserIds = manager.getFormResponseAndUserIds(formId, questionId, formResponsesIdentifier, pageable);

        var userIds = resAndUserIds.stream().map(tuple -> tuple.get("userId", UUID.class)).toList();

        var userSummaries = authServiceFeignClient.userSummaries(userIds).getUsers();

        var userSummariesMap = new HashMap<UUID, UserSummaryShortDto>();
        userSummaries.forEach(userSummary -> userSummariesMap.put(userSummary.getId(), userSummary));

        var formResponseSummaries = new ArrayList<FormResponseSummaryDto>();

        resAndUserIds.forEach(tuple -> {
            var resId = tuple.get("responseId", Long.class);
            var userId = tuple.get("userId", UUID.class);

            var user = Optional.ofNullable(userSummariesMap.get(userId)).orElse(new UserSummaryShortDto(null, null));

            formResponseSummaries.add(
                    new FormResponseSummaryDto(
                            resId,
                            user.getId(),
                            user.getUserName()
                    )
            );
        });

        return new FormResponseSummariesDto(formResponseSummaries);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseIndividualResDto getIndividualFormResponse(UUID formId, Long formResponseId) {
        return getIndividualFormResponseHelper(formId, formResponseId, null);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseIndividualResDto getIndividualFormResponseByPage(UUID formId, Long page) {

        var formResponseId = formResponseRepository.getFormResponseIdFromPage(formId, page)
                .orElseThrow(() -> new IllegalArgumentException("Form response not found for page: " + page));

        var indiFormResponseCacheKey = CacheUtil.buildKey(FormResponseCacheNames.INDIVIDUAL_FORM_RESPONSE, "formId=" + formId, "formResponseId=" + formResponseId);

        var indiFormResponseCached = (ResponseIndividualResDto) redisTemplate.opsForValue().get(indiFormResponseCacheKey);

        if (indiFormResponseCached != null) {
            return indiFormResponseCached;
        }

        var indiFormResponse = getIndividualFormResponseHelper(formId, formResponseId, page);

        redisTemplate.opsForValue().set(indiFormResponseCacheKey, indiFormResponse, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));

        return indiFormResponse;
    }

    @Override
    public ResponseQuestionDto<? extends ResponseByQuestionResponse> getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var qSummary = formServiceFeignClient.getQuestionSummary(formId, questionId);
        var manager = responseManagerFactory.get(qSummary.getQuestionType());

        return manager.getResponseByQuestion(formId, qSummary.getId(), extraParams, pageable);
    }

    @Override
    public FormResponseCountDto getFormResponseCount(UUID formId) {
        return new FormResponseCountDto(formResponseRepository.getFormResponseCount(formId));
    }

    private ResponseIndividualResDto getIndividualFormResponseHelper(UUID formId, Long formResponseId, Long formResponsePage) {

        var formResponse = formResponseRepository.findById(formResponseId)
                .orElseThrow(() -> new IllegalArgumentException("Form response not found with ID: " + formResponseId));

        var formResponseQuestionTypes = questionResponseRepository.findDistinctQuestionTypesByFormResponseId(formResponseId);

        var result = new ArrayList<ResponseIndividualDto>();

        formResponseQuestionTypes.forEach(qType -> {
            var manager = responseManagerFactory.get(qType);

            var indiResponses = manager.getIndividualResponses(formId, formResponseId);

            result.addAll(indiResponses);
        });

        var finalFormResponsePage = formResponsePage == null ? formResponseRepository.getPageNumberOfFormResponse(formId, formResponseId)
                .orElseThrow(() -> new IllegalArgumentException("Form response not found with ID: " + formResponseId)) : formResponsePage;

        return new ResponseIndividualResDto(formResponseId, finalFormResponsePage, formResponse.getUserId(), result);
    }
}
