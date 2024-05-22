package com.fzemi.sscmaster.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    /**
     * Internal error code
     */
    private Integer errorCode;

    /**
     * Error description
     */
    private String errorDescription;

    /**
     * Error message
     */
    private String error;

    /**
     * Validation errors for request body
     */
    private Set<String> validationErrors;
}
