package com.kupreychik.parcellapp.controller;

import com.kupreychik.parcellapp.dto.UiErrorDTO;
import com.kupreychik.parcellapp.exception.ParcelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<UiErrorDTO> handleCredentialsExpiredException(CredentialsExpiredException ex) {
        var uiErrorDTO = new UiErrorDTO("Invalid username or password", 0);
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.status(UNAUTHORIZED).body(uiErrorDTO);
    }


    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<UiErrorDTO> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        var uiErrorDTO = new UiErrorDTO("Invalid username or password", 0);
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.status(UNAUTHORIZED).body(uiErrorDTO);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<UiErrorDTO> handleAccessDeniedException(AccessDeniedException ex) {
        var uiErrorDTO = new UiErrorDTO("Access denied", 401);
        log.warn(ex.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(uiErrorDTO);
    }
}
