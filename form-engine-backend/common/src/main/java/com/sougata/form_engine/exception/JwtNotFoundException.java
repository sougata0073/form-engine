package com.sougata.form_engine.exception;

public class JwtNotFoundException extends RuntimeException {
    public JwtNotFoundException(String message) {
        super(message);
    }
}
