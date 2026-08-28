package com.sougata.form_data_service.formValidation.exception;

import com.sougata.form_data_service.constant.ExceptionMessages;

import java.util.List;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(String uploadedFileType, List<String> allowedFileTypes) {
        super(String.format(ExceptionMessages.INVALID_FILE_TYPE, uploadedFileType, allowedFileTypes));
    }
}
