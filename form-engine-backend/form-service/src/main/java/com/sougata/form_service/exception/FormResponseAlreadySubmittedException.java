package com.sougata.form_service.exception;

import com.sougata.form_engine.dto.form.ViewFormErrorResDto;
import lombok.Getter;

@Getter
public class FormResponseAlreadySubmittedException extends RuntimeException {
    private final ViewFormErrorResDto dto;

    public FormResponseAlreadySubmittedException(ViewFormErrorResDto dto) {
        this.dto = dto;
    }
}
