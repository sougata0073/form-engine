package com.sougata.form_data_service.formValidation.responseValidator;

import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import com.sougata.form_engine.dto.validation.config.ValidationConfig;

public interface ResponseValidator <V extends QuestionResponsePutReqDto, C extends ValidationConfig> {
    boolean isValid(V validationRequestDto, C validationConfig);
}
