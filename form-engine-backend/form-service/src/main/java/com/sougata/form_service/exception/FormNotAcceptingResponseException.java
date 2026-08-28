package com.sougata.form_service.exception;

import com.sougata.form_service.dto.form.ViewFormErrorResDto;
import lombok.Getter;

@Getter
public class FormNotAcceptingResponseException extends RuntimeException {

    private final ViewFormErrorResDto dto;

    public FormNotAcceptingResponseException(ViewFormErrorResDto dto) {
        this.dto = dto;
    }
}
