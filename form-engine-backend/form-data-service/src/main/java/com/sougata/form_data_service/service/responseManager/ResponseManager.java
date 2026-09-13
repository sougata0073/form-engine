package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.QuestionResponse;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;

public abstract class ResponseManager<QR extends QuestionResponsePutReqDto> {

    private final QuestionResponseRepository questionResponseRepository;

    public ResponseManager(QuestionResponseRepository questionResponseRepository) {
        this.questionResponseRepository = questionResponseRepository;
    }

    public abstract void create(QR response, FormResponse formResponse);

    public abstract void deleteResponsesByQuestionId(Long questionId);

    public abstract void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId);

    public abstract QuestionType getQuestionType();

    public QuestionResponse createQuestionResponse(Long questionId, FormResponse formResponse) {
        var qr = new QuestionResponse();

        var partitionKey = new QuestionResponse.PartitionKey();

        partitionKey.setQuestionId(questionId);
        partitionKey.setFormResponseId(formResponse.getKey().getFormResponseId());

        qr.setKey(partitionKey);
        qr.setQuestionType(getQuestionType());

        return questionResponseRepository.save(qr);
    }

}
