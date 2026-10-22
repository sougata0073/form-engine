package com.sougata.form_response_service.service.impl;

import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_engine.dto.form.FormResponseCountDto;
import com.sougata.form_engine.dto.form.FormResponseSummariesDto;
import com.sougata.form_engine.dto.form.FormResponseSummaryDto;
import com.sougata.form_engine.dto.formResponse.individual.FormResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.ResponseByQuestionResponse;
import com.sougata.form_engine.dto.formResponse.question.ResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryResDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.user.UserSummaryShortDto;
import com.sougata.form_response_service.configuration.AppConfiguration;
import com.sougata.form_response_service.feignClient.AuthServiceFeignClient;
import com.sougata.form_response_service.feignClient.FormDataServiceFeignClient;
import com.sougata.form_response_service.feignClient.FormServiceFeignClient;
import com.sougata.form_response_service.repository.FormResponseIndividualRepository;
import com.sougata.form_response_service.repository.FormResponseSummaryRepository;
import com.sougata.form_response_service.service.FormResponseService;
import com.sougata.form_response_service.service.responseManager.ResponseManagerFactory;
import com.sougata.form_response_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormResponseServiceImpl implements FormResponseService {

    private final FormResponseSummaryRepository formResponseSummaryRepository;
    private final FormServiceFeignClient formServiceFeignClient;
    private final AuthServiceFeignClient authServiceFeignClient;
    private final ResponseManagerFactory responseManagerFactory;
    private final FormDataServiceFeignClient formDataServiceFeignClient;
    private final FormResponseIndividualRepository formResponseIndividualRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AppConfiguration appConfiguration;

    @Override
    public boolean getIsResponseAlreadySubmitted(UUID formId, UUID userId) {
        return formResponseIndividualRepository.existsByFormIdAndUserId(formId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseSummaryResDto getResponseSummaries(UUID formId) {
        var questions = formServiceFeignClient.getFormDetails(formId).getQuestions();

        var questionTypeMap = questions.stream().collect(Collectors.groupingBy(QuestionDetailsDto::getQuestionType));

        var result = new ArrayList<ResponseSummaryDto<?>>();

        questionTypeMap.keySet().forEach(qType -> {
            var filteredQuestions = questionTypeMap.get(qType);

            var manager = responseManagerFactory.get(qType);
            var summaries = manager.getResponseSummaries(formId, filteredQuestions);

            if (summaries != null) {
                result.addAll(summaries);
            }
        });

        result.sort(Comparator.comparingInt(ResponseSummaryDto::getOrderIndex));

        return new ResponseSummaryResDto(result);
    }

    @Override
    public ResponseSummaryDto<?> getResponseSummary(UUID formId, Long questionId, Pageable pageable) {
        var question = formServiceFeignClient.getQuestion(formId, questionId);
        var manager = responseManagerFactory.get(question.getQuestionType());

        var resSummary = manager.getResponseSummary(questionId, question, pageable);

        resSummary.setQuestionId(question.getId());
        resSummary.setQuestion(question.getQuestion());
        resSummary.setOrderIndex(question.getOrderIndex());
        resSummary.setQuestionType(question.getQuestionType());

        return resSummary;
    }

    @Override
    public FormResponseSummariesDto getFormResponseSummaries(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
        var questionSummary = formServiceFeignClient.getQuestionSummary(formId, questionId);
        var manager = responseManagerFactory.get(questionSummary.getQuestionType());

        var resAndUserIds = manager.getFormResponseAndUserIds(formId, questionId, formResponsesIdentifier, pageable);

        var userIds = resAndUserIds.stream().map(tuple -> tuple.get("userId", UUID.class)).toList();

        var userSummaries = authServiceFeignClient.userSummaries(userIds).getUsers();

        var userSummariesMap = userSummaries.stream().collect(Collectors.toMap(UserSummaryShortDto::getId, Function.identity()));

        var formResponseSummaries = new ArrayList<FormResponseSummaryDto>();

        resAndUserIds.forEach(tuple -> {
            var resId = tuple.get("responseId", UUID.class);
            var userId = tuple.get("userId", UUID.class);

            var user = userSummariesMap.getOrDefault(userId, new UserSummaryShortDto(null, null));

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
    public FormResponseIndividualDto getIndividualFormResponse(UUID formId, UUID formResponseId) {
        var formResponsePage = formResponseIndividualRepository.getFormResponsePageByFormResponseId(formId, formResponseId);

        if (formResponsePage == null) {
            throw new RuntimeException("Form response page not found for form ID: " + formId + " and form response ID: " + formResponseId);
        }

        var formResponseNoPage = formDataServiceFeignClient.getFormResponse(formId, formResponseId);

        return new FormResponseIndividualDto(
                formResponseNoPage.getFormResponseId(),
                formResponsePage,
                formResponseNoPage.getUserId(),
                formResponseNoPage.getResponses()
        );
    }

    @Override
    public FormResponseIndividualDto getIndividualFormResponseByPage(UUID formId, Long page) {
        var formResponseId = formResponseIndividualRepository.getFormResponseIdByFormResponsePage(formId, page);

        if (formResponseId == null) {
            throw new RuntimeException("Form response page not found for form ID: " + formId + " and form response page: " + page);
        }

        var formResponseNoPage = formDataServiceFeignClient.getFormResponse(formId, formResponseId);

        var individualFormResponse = new FormResponseIndividualDto(
                formResponseNoPage.getFormResponseId(),
                page,
                formResponseNoPage.getUserId(),
                formResponseNoPage.getResponses()
        );

        var individualFormResponseByPageCacheKey = CacheUtil.buildKey(
                FormResponseCacheNames.INDIVIDUAL_FORM_RESPONSE_BY_PAGE, "formId=" + formId, "formResponseId=" + formResponseId, "page=" + page
        );

        redisTemplate.opsForValue().set(
                individualFormResponseByPageCacheKey,
                individualFormResponse,
                Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes())
        );

        return individualFormResponse;
    }

    @Override
    public ResponseQuestionDto<? extends ResponseByQuestionResponse> getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var qSummary = formServiceFeignClient.getQuestionSummary(formId, questionId);
        var manager = responseManagerFactory.get(qSummary.getQuestionType());

        var resByQuestion = manager.getResponseByQuestion(formId, qSummary.getId(), extraParams, pageable);

        resByQuestion.setQuestionId(qSummary.getId());
        resByQuestion.setQuestionType(qSummary.getQuestionType());

        resByQuestion.getResponses().forEach(res -> {
            res.setQuestionId(qSummary.getId());
            res.setQuestionType(qSummary.getQuestionType());
        });

        return resByQuestion;
    }

    @Override
    public FormResponseCountDto getFormResponseCount(UUID formId) {
        return new FormResponseCountDto(
                formResponseSummaryRepository.findFormResponseCountByFormId(formId)
        );
    }

}
