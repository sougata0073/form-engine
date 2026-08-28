package com.sougata.form_service.exception;

import com.sougata.form_service.constant.ExceptionMessages;

public class FileTypeNotFoundException extends RuntimeException {
    public FileTypeNotFoundException(String fileCategory) {
        super(String.format(ExceptionMessages.FILE_TYPE_NOT_FOUND, fileCategory));
    }
}
