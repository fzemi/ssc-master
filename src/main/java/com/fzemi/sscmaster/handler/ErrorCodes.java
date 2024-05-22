package com.fzemi.sscmaster.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodes {
    // 10xx - USERS ERRORS
    USER_NOT_FOUND(1000, HttpStatus.NOT_FOUND, "User not found"),
    // 11xx - TASKS ERRORS
    TASK_NOT_FOUND(1100, HttpStatus.NOT_FOUND, "Task not found"),
    // OTHER
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code")
    ;

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    ErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
