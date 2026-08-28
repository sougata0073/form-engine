package com.sougata.form_data_service.exception;


public class JsonParsingException extends RuntimeException {
    public JsonParsingException(String json) {
        super("Error parsing JSON: " + json);
    }
}
