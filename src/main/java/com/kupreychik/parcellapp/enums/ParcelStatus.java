package com.kupreychik.parcellapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Parcel status
 */
@Getter
@AllArgsConstructor
public enum ParcelStatus {
    NEW("New", true),
    ASSIGNED("Assigned", true),
    IN_PROGRESS("In progress", true),
    DELIVERED("Delivered", false),
    CANCELED("Canceled", false);

    /**
     * Name of status. Used to show to user
     */
    private final String name;

    private final boolean isAvailableToCancel;

}
