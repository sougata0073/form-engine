package com.sougata.auth_service.exception;

import com.sougata.auth_service.constant.ExceptionMessages;

public class WrongUsernameException extends RuntimeException {
    public WrongUsernameException() {
        super(ExceptionMessages.WRONG_USERNAME);
    }
}
