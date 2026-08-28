package com.sougata.form_data_service.exception;

public class FormResponseAlreadySubmittedException extends RuntimeException {
    public FormResponseAlreadySubmittedException(String message) {
        super(message);
    }
}
