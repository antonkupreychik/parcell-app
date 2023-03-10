package com.kupreychik.parcellapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Ui error. Used to throw exceptions with ui error
 */
@Getter
@AllArgsConstructor
public enum UiError {

    ROLE_NOT_FOUND("Role not found", 1);

    /**
     * Message of error which will be shown to user
     */
    private final String message;
    /**
     * Specific code of error. Need to identify error on client side
     */
    private final int code;
}
