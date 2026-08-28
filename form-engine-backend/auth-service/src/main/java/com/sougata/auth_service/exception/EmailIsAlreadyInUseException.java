package com.sougata.auth_service.exception;

public class EmailIsAlreadyInUseException extends RuntimeException {
    public EmailIsAlreadyInUseException(String message) {
        super(message);
    }
}
