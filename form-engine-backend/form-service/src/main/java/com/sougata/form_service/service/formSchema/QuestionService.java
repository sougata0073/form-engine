package com.sougata.form_service.service.formSchema;

import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.schemaputrequest.QuestionOrderUpdateReqDto;
import com.sougata.form_engine.dto.question.schemaputrequest.QuestionPutReqDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummariesDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummaryDto;

import java.util.UUID;

public interface QuestionService {
    QuestionDetailsDto createQuestion(UUID formId, QuestionPutReqDto dto);

    QuestionDetailsDto updateQuestion(UUID formId, Long questionId, QuestionPutReqDto dto);

    SuccessMessageDto deleteQuestion(UUID formId, Long questionId);

    QuestionDetailsDto getQuestion(UUID formId, Long questionId);

    QuestionSummariesDto getQuestionSummaries(UUID formId);

    QuestionSummaryDto getQuestionSummary(UUID formId, Long questionId);

    SuccessMessageDto updateOrderIndex(UUID formId, Long questionId, QuestionOrderUpdateReqDto req);
}
