package com.kupreychik.parcellapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Parcel exception. Used to throw exceptions with ui error
 */
@Getter
@AllArgsConstructor
public class ParcelException extends RuntimeException {

    /**
     * Ui error
     */
    private final UiError uiError;
}
