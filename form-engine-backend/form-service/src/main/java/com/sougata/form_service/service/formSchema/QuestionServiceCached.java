package com.sougata.form_service.service.formSchema;

import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummariesDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummaryDto;

import java.util.UUID;

public interface QuestionServiceCached {

    QuestionDetailsDto getQuestionDetails(UUID formId, Long questionId);

    QuestionSummariesDto getQuestionSummaries(UUID formId);

    QuestionSummaryDto getQuestionSummary(UUID formId, Long questionId);


}
