package com.kupreychik.parcellapp.controller;

import com.kupreychik.parcellapp.dto.UiErrorDTO;
import com.kupreychik.parcellapp.exception.ParcelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception controller. Used to handle exceptions
 */
@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public UiErrorDTO handleException(Exception e) {
        var uiErrorDTO = new UiErrorDTO("Something went wrong", 500);
        log.error("Something went wrong. Exception: {}", e.getMessage());
        return uiErrorDTO;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParcelException.class)
    public UiErrorDTO handleParcelException(ParcelException e) {
        var uiErrorDTO = new UiErrorDTO(e.getUiError().getMessage(), e.getUiError().getCode());
        log.warn("Parcel exception: {}", e.getUiError().getMessage());
        return uiErrorDTO;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public UiErrorDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        var uiErrorDTO = new UiErrorDTO("Validation error", 0, excludeErrorsFromConstraint(ex));
        log.warn("Validation error: {}", ex.getMessage());
        return uiErrorDTO;
    }

    private static Map<String, String> excludeErrorsFromConstraint(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
