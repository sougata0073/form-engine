package com.sougata.form_data_service.formValidation.service;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;

public abstract class QuestionSchemaManager<QR extends QuestionDetailsDto, QRAR extends QuestionResponsePutReqDto> {

    public abstract boolean validateResponse(QRAR questionResponseAddReq, QR questionRes);

    public abstract QuestionType getQuestionType();
}
