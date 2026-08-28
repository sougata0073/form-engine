package com.sougata.form_service.exception;

import com.sougata.form_service.constant.ExceptionMessages;

public class InvalidFileSizeException extends RuntimeException {
    public InvalidFileSizeException(int uploadedFileSize, int maxFileSize) {
        super(String.format(ExceptionMessages.INVALID_FILE_SIZE, uploadedFileSize, maxFileSize));
    }
}
