package com.sougata.form_service.service.formSchema;

import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.question.details.MultipleQuestionDetailsDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.QuestionAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.MultipleQuestionUpdateReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.QuestionOrderUpdateReqDto;

import java.util.UUID;

public interface QuestionService {
    QuestionDetailsDto createQuestion(UUID formId, UUID userId, QuestionAddReqDto dto);

    MultipleQuestionDetailsDto updateQuestions(UUID formId, MultipleQuestionUpdateReqDto dto);

    QuestionDetailsDto copyQuestion(UUID formId, Long questionId);

    SuccessMessageDto deleteQuestion(UUID formId, Long questionId);

    SuccessMessageDto updateOrderIndex(UUID formId, Long questionId, QuestionOrderUpdateReqDto req);
}
