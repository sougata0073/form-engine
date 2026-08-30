package com.sougata.form_service.exception;

import com.sougata.form_engine.dto.form.ViewFormErrorResDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FormNotAcceptingResponseException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ViewFormErrorResDto handleFormNotAcceptingResponseException(FormNotAcceptingResponseException e) {
        return e.getDto();
    }

    @ExceptionHandler(FormResponseAlreadySubmittedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ViewFormErrorResDto handleFormResponseAlreadySubmittedException(FormResponseAlreadySubmittedException e) {
        return e.getDto();
    }

}
