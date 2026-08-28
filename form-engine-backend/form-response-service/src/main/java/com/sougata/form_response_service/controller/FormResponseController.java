package com.sougata.form_response_service.controller;

import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_engine.dto.form.FormResponseCountDto;
import com.sougata.form_engine.dto.form.FormResponseSummariesDto;
import com.sougata.form_engine.dto.formResponse.individual.ResponseIndividualResDto;
import com.sougata.form_engine.dto.formResponse.question.ResponseByQuestionResponse;
import com.sougata.form_engine.dto.formResponse.question.ResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryResDto;
import com.sougata.form_response_service.service.FormResponseService;
import jakarta.ws.rs.QueryParam;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/forms")
@CrossOrigin
@RequiredArgsConstructor
public class FormResponseController {

    private final FormResponseService formResponseService;

    @GetMapping(path = "{formId}/is-response-already-submitted", params = "userId")
    @Cacheable(
            cacheNames = {FormResponseCacheNames.IS_RESPONSE_ALREADY_SUBMITTED},
            key = "'formId=' + #formId + '::userId=' + #userId"
    )
    public boolean getIsResponseAlreadySubmitted(
            @PathVariable("formId") UUID formId,
            @RequestParam("userId") UUID userId
    ) {
        return formResponseService.getIsResponseAlreadySubmitted(formId, userId);
    }

    @GetMapping(path = "{formId}/form-response-count")
    @Cacheable(cacheNames = {FormResponseCacheNames.FORM_RESPONSE_COUNT}, key = "#formId")
    public FormResponseCountDto getFormResponseCount(
            @PathVariable("formId") UUID formId
    ) {
        return formResponseService.getFormResponseCount(formId);
    }

    @GetMapping(path = "{formId}/response-summaries")
    @Cacheable(cacheNames = {FormResponseCacheNames.RESPONSE_SUMMARIES}, key = "#formId")
    public ResponseSummaryResDto getResponseSummaries(
            @PathVariable("formId") UUID formId
    ) {
        return formResponseService.getResponseSummaries(formId);
    }

    @GetMapping(path = "{formId}/questions/{questionId}/response-summary")
    @Cacheable(
            cacheNames = {FormResponseCacheNames.RESPONSE_SUMMARY},
            key = "'formId=' + #formId + '::questionId=' + #questionId + '::pageNumber=' + #pageable.pageNumber + '::pageSize=' + #pageable.pageSize"
    )
    public ResponseSummaryDto<?> getResponseSummary(
            @PathVariable("formId") UUID formId,
            @PathVariable("questionId") Long questionId,
            Pageable pageable
    ) {
        return formResponseService.getResponseSummary(formId, questionId, pageable);
    }

    @GetMapping(path = "{formId}/questions/{questionId}/form-response-summaries", params = {"formResponsesIdentifier"})
    @Cacheable(
            cacheNames = {FormResponseCacheNames.FORM_RESPONSE_SUMMARIES},
            key = "'formId=' + #formId + '::questionId=' + #questionId + '::formResponseIdentifier=' + #formResponsesIdentifier + '::pageNumber=' + #pageable.pageNumber + '::pageSize=' + #pageable.pageSize"
    )
    public FormResponseSummariesDto getFormResponseSummaries(
            @PathVariable("formId") UUID formId,
            @PathVariable("questionId") Long questionId,
            @RequestParam("formResponsesIdentifier") String formResponsesIdentifier,
            Pageable pageable
    ) {
        return formResponseService.getFormResponseSummaries(formId, questionId, formResponsesIdentifier, pageable);
    }

    @GetMapping(path = "{formId}/responses/{formResponseId}")
    @Cacheable(
            cacheNames = {FormResponseCacheNames.INDIVIDUAL_FORM_RESPONSE},
            key = "'formId=' + #formId + '::formResponseId=' + #formResponseId"
    )
    public ResponseIndividualResDto getIndividualFormResponse(
            @PathVariable("formId") UUID formId,
            @PathVariable("formResponseId") Long formResponseId
    ) {
        return formResponseService.getIndividualFormResponse(formId, formResponseId);
    }

    // Page number starts from 0
    @GetMapping(path = "{formId}/responses", params = {"page"})
    public ResponseIndividualResDto getIndividualFormResponseByPage(
            @PathVariable("formId") UUID formId,
            @QueryParam("page") Long page
    ) {
        return formResponseService.getIndividualFormResponseByPage(formId, page);
    }

    @GetMapping(path = "{formId}/questions/{questionId}/response")
    @Cacheable(
            cacheNames = {FormResponseCacheNames.RESPONSE_BY_QUESTION},
            key = "'formId=' + #formId + '::questionId=' + #questionId + '::extraParams=' + #extraParams.entrySet()"
    )
    public ResponseQuestionDto<? extends ResponseByQuestionResponse> getResponseByQuestion(
            @PathVariable("formId") UUID formId,
            @PathVariable("questionId") Long questionId,
            @RequestParam Map<String, String> extraParams,
            Pageable pageable
    ) {
        return formResponseService.getResponseByQuestion(formId, questionId, extraParams, pageable);
    }

}
