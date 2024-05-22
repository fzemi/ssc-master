package com.fzemi.sscmaster.task.exception;

import com.fzemi.sscmaster.handler.AppException;
import com.fzemi.sscmaster.handler.ErrorCodes;

public class TaskNotFoundException extends AppException {

    public TaskNotFoundException(String message) {
        super(ErrorCodes.TASK_NOT_FOUND, message);
    }
}
