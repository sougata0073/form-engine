package com.sougata.form_service.service.formSchema;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.common.SuccessMessageDto;
import com.sougata.form_service.dto.question.QuestionSummariesDto;
import com.sougata.form_service.dto.question.QuestionSummaryDto;
import com.sougata.form_service.dto.question.request.QuestionOrderUpdateReqDto;
import com.sougata.form_service.dto.question.request.QuestionPutReqDto;
import com.sougata.form_service.dto.question.response.QuestionDetails;
import com.sougata.form_service.model.formSchema.Question;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    QuestionDetails createQuestion(UUID formId, QuestionPutReqDto dto);

    QuestionDetails updateQuestion(UUID formId, Long questionId, QuestionPutReqDto dto);

    SuccessMessageDto deleteQuestion(UUID formId, Long questionId);

    QuestionDetails getQuestion(UUID formId, Long questionId);

    List<QuestionDetails> getSimilarTypeQuestions(QuestionType questionType, List<Question> parentQuestions);

    QuestionSummariesDto getQuestionSummaries(UUID formId);

    QuestionSummaryDto getQuestionSummary(UUID formId, Long questionId);

    SuccessMessageDto updateOrderIndex(UUID formId, Long questionId, QuestionOrderUpdateReqDto req);
}
