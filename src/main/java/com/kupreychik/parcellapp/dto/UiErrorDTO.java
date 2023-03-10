package com.kupreychik.parcellapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Ui error dto. Used to show errors to user
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UiErrorDTO {

    /**
     * Message of error which will be shown to user
     */
    private String message;
    /**
     * Specific code of error. Need to identify error on client side
     */
    private int code;
    /**
     * Validation errors. Used to show validation errors to user
     */
    private Map<String, String> validationErrors;

    /**
     * Constructor for ui error dto without validation errors
     *
     * @param message message of error which will be shown to user
     * @param code   specific code of error. Need to identify error on client side
     */
    public UiErrorDTO(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
