package com.fzemi.sscmaster.user.exception;

import com.fzemi.sscmaster.handler.AppException;
import com.fzemi.sscmaster.handler.ErrorCodes;

public class UserNotFoundException extends AppException {

    public UserNotFoundException(String message) {
        super(ErrorCodes.USER_NOT_FOUND, message);
    }
}
