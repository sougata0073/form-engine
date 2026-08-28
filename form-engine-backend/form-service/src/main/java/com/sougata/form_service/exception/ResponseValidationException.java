package com.sougata.form_service.exception;

public class ResponseValidationException extends RuntimeException {
    public ResponseValidationException(String message) {
        super(message);
    }
}
