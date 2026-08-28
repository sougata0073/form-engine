package com.sougata.auth_service.exception;

import com.sougata.auth_service.constant.ExceptionMessages;

public class UserRoleMismatchException extends RuntimeException {
    public UserRoleMismatchException() {
        super(ExceptionMessages.USER_ROLE_MISMATCH);
    }
}
