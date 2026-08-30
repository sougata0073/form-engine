package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.QuestionResponse;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseRequest.QuestionResponsePutReqDto;

import java.util.UUID;

public abstract class ResponseManager<QR extends QuestionResponsePutReqDto> {

    private final QuestionResponseRepository questionResponseRepository;

    public ResponseManager(QuestionResponseRepository questionResponseRepository) {
        this.questionResponseRepository = questionResponseRepository;
    }

    public abstract void create(QR response, FormResponse formResponse);

    public abstract void deleteResponsesByQuestion(UUID formId, Long questionId);

    public abstract void deleteResponsesByFormResponse(UUID formId, Long formResponseId);

    public abstract QuestionType getQuestionType();

    public QuestionResponse createQuestionResponse(Long questionId, FormResponse formResponse) {
        var qr = new QuestionResponse();

        qr.setQuestionId(questionId);
        qr.setFormResponse(formResponse);
        qr.setQuestionType(getQuestionType());

        return questionResponseRepository.save(qr);
    }

}
