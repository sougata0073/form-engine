package com.sougata.form_data_service.formValidation.service;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.dto.question.request.QuestionResponsePutReqDto;
import com.sougata.form_data_service.dto.question.response.QuestionDetailsDto;

public abstract class QuestionSchemaManager<QR extends QuestionDetailsDto, QRAR extends QuestionResponsePutReqDto> {

    public abstract boolean validateResponse(QRAR questionResponseAddReq, QR questionRes);

    public abstract QuestionType getQuestionType();
}
