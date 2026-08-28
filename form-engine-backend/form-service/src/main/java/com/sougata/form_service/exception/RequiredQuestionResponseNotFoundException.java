package com.sougata.form_service.exception;

import java.util.List;

public class RequiredQuestionResponseNotFoundException extends RuntimeException {
    public RequiredQuestionResponseNotFoundException(List<String> missingQuestionIds) {
        super(String.join(", ", missingQuestionIds));
    }
}
