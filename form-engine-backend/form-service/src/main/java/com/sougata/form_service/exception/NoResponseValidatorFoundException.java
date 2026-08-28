package com.sougata.form_service.exception;

import com.sougata.form_service.constant.ExceptionMessages;

public class NoResponseValidatorFoundException extends RuntimeException {
    public NoResponseValidatorFoundException(String validationId) {
        super(String.format(ExceptionMessages.RESPONSE_VALIDATOR_NOT_FOUND, validationId));
    }
}
