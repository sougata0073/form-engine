package com.sougata.form_response_service.service;

import com.sougata.form_engine.dto.form.FormResponseCountDto;
import com.sougata.form_engine.dto.form.FormResponseSummariesDto;
import com.sougata.form_engine.dto.formResponse.individual.ResponseIndividualResDto;
import com.sougata.form_engine.dto.formResponse.question.ResponseByQuestionResponse;
import com.sougata.form_engine.dto.formResponse.question.ResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryResDto;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface FormResponseService {

    ResponseSummaryResDto getResponseSummaries(UUID formId);

    ResponseSummaryDto<?> getResponseSummary(UUID formId, Long questionId, Pageable pageable);

    FormResponseSummariesDto getFormResponseSummaries(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable);

    ResponseIndividualResDto getIndividualFormResponse(UUID formId, Long formResponseId);

    ResponseIndividualResDto getIndividualFormResponseByPage(UUID formId, Long page);

    ResponseQuestionDto<? extends ResponseByQuestionResponse> getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable);

    FormResponseCountDto getFormResponseCount(UUID formId);

    boolean getIsResponseAlreadySubmitted(UUID formId, UUID userId);
}
