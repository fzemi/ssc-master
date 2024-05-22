package com.fzemi.sscmaster.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles request validation errors
     *
     * @param exc validation exception
     * @return response with validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exc) {
        Set<String> errors = new HashSet<>();
        exc.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        return ResponseEntity.status(BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .errorCode(BAD_REQUEST.value())
                        .validationErrors(errors)
                        .build()
        );
    }

    /**
     * Handles custom exceptions thrown by the application in business logic
     *
     * @param exc custom exception
     * @return response with error message
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionResponse> handleException(AppException exc) {
        ErrorCodes errorCode = exc.getErrorCode();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .errorCode(errorCode.getCode())
                        .errorDescription(errorCode.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    /**
     * Handles all other exceptions
     *
     * @param exc exception
     * @return response with error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exc) {
        exc.printStackTrace(); // Log the exception
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .errorCode(INTERNAL_SERVER_ERROR.value())
                        .errorDescription("Internal server error")
                        .error(exc.getMessage())
                        .build()
        );
    }
}
