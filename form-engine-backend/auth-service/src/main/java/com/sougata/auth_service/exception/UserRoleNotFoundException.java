package com.sougata.auth_service.exception;

import com.sougata.auth_service.constant.ExceptionMessages;

public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException() {
        super(ExceptionMessages.USER_ROLE_NOT_FOUND);
    }
}
