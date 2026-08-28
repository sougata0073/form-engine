package com.sougata.form_service.exception;

public class TemplateNotFoundException extends RuntimeException {
    public TemplateNotFoundException(Long templateId) {
        super("Template not found with ID: " + templateId);
    }
}
