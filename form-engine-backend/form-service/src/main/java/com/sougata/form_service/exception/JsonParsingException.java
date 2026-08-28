package com.sougata.form_service.exception;

import com.sougata.form_service.constant.ExceptionMessages;

public class JsonParsingException extends RuntimeException {
    public JsonParsingException(String json) {
        super(String.format(ExceptionMessages.JSON_PARSING_EXCEPTION, json));
    }
}
