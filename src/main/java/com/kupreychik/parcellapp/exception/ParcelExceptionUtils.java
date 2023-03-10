package com.kupreychik.parcellapp.exception;

/**
 * Parcel exception utils. Used to create parcel exception
 */
public class ParcelExceptionUtils {

    /**
     * Convert ui error to parcel exception
     *
     * @param uiError ui error
     * @return parcel exception
     */
    public static ParcelException createParcelException(UiError uiError) {
        return new ParcelException(uiError);
    }

    /**
     * Private constructor
     */
    private ParcelExceptionUtils() {
    }
}
