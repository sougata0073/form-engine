package com.sougata.form_data_service.formValidation.responseValidator;

import com.sougata.form_data_service.dto.question.request.QuestionResponsePutReqDto;
import com.sougata.form_data_service.dto.validationConfig.ValidationConfig;

public interface ResponseValidator <V extends QuestionResponsePutReqDto, C extends ValidationConfig> {
    boolean isValid(V validationRequestDto, C validationConfig);
    Class<V> getValidationRequestClass();
    Class<C> getValidationConfigClass();
}
