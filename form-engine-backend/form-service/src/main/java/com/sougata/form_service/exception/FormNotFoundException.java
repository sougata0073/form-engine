package com.sougata.form_service.exception;

import com.sougata.form_service.constant.ExceptionMessages;

import java.util.UUID;

public class FormNotFoundException extends RuntimeException {
    public FormNotFoundException(UUID formId) {
        super(String.format(ExceptionMessages.FORM_NOT_FOUND, formId.toString()));
    }
}
