package com.sougata.form_data_service.formValidation.exception;

import java.util.List;

public class RequiredQuestionResponseNotFoundException extends RuntimeException {
    public RequiredQuestionResponseNotFoundException(List<String> missingQuestionIds) {
        super(String.join(", ", missingQuestionIds));
    }
}
