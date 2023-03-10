package com.kupreychik.parcellapp.service;

import com.kupreychik.parcellapp.enums.ParcelStatus;

public interface ParcelHistoryService {
    void changeStatus(Long parcelId, ParcelStatus status);
}
