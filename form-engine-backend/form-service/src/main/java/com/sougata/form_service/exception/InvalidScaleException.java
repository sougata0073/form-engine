package com.sougata.form_service.exception;

import com.sougata.form_service.constant.ExceptionMessages;

public class InvalidScaleException extends RuntimeException {
    public InvalidScaleException(int maxScale, int providedScale) {
        super(String.format(ExceptionMessages.INVALID_SCALE, maxScale, providedScale));
    }
}
