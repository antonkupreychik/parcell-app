package com.kupreychik.parcellapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Parcel status
 */
@Getter
@AllArgsConstructor
public enum ParcelStatus {
    NEW("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In progress"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    /**
     * Name of status. Used to show to user
     */
    private final String name;

}
