package com.kupreychik.parcellapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Ui error. Used to throw exceptions with ui error
 */
@Getter
@AllArgsConstructor
public enum UiError {

    ROLE_NOT_FOUND("Role not found", 1),
    EMAIL_ALREADY_EXIST("Email already exist", 2),
    USER_NOT_FOUND("User not found", 3),
    ADDRESS_NOT_FOUND("Address not found", 4),
    PARCEL_NOT_AVAILABLE_TO_CHANGE_ADDRESS("Parcel not available to change address", 5),
    PARCEL_NOT_AVAILABLE_TO_CANCEL("Parcel not available to cancel", 6),
    USER_IS_NOT_COURIER("User is not courier", 7),
    PARCEL_NOT_FOUND("Parcel not found", 8),
    PARCEL_NOT_AVAILABLE_TO_ASSIGN_COURIER("Parcel not available to assign courier", 9),
    COURIER_LIMIT_PER_PARCEL_EXCEEDED("Courier limit per parcel exceeded", 10),
    PARCEL_ALREADY_ASSIGNED_TO_COURIER("Parcel already assigned to courier", 11),
    MAX_PARCEL_COUNT_REACHED("Max parcel count reached", 12),
    CONFIG_NOT_FOUND("Config not found", 13),
    PARCEL_PARAMS_NOT_VALID("Parcel params not valid", 14),
    OPERATION_TYPE_NOT_FOUND("Operation type not found", 15),
    BALANCE_IS_NOT_ENOUGH("Balance is not enough", 16),
    MIN_PRICE_PER_ONE_PARCEL_REACHED("Min price per one parcel reached", 17),
    MAX_PRICE_PER_ONE_PARCEL_REACHED("Max price per one parcel reached", 18),
    PARCEL_PRICE_CALCULATION_ERROR("Parcel price calculation error", 19);
    /**
     * Message of error which will be shown to user
     */
    private final String message;
    /**
     * Specific code of error. Need to identify error on client side
     */
    private final int code;
}
