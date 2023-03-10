package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.enums.ParcelStatus;
import com.kupreychik.parcellapp.model.ParcelHistory;
import com.kupreychik.parcellapp.repository.ParcelHistoryRepository;
import com.kupreychik.parcellapp.service.ParcelHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParcelHistoryServiceImpl implements ParcelHistoryService {

    private final ParcelHistoryRepository parcelHistoryRepository;

    @Override
    @Transactional
    public void changeStatus(Long parcelId, ParcelStatus status) {
        try {
            log.info("Changing parcel status. Parcel id: {}, status: {}", parcelId, status);
            var parcelHistory = buildParcelHistory(parcelId, status);
            parcelHistoryRepository.save(parcelHistory);
            log.info("Parcel status changed. Parcel id: {}, status: {}", parcelId, status);
        } catch (Exception e) {
            log.error("Error while changing parcel status. Parcel id: {}, status: {}", parcelId, status, e);
            throw e;
        }
    }

    private ParcelHistory buildParcelHistory(Long parcelId, ParcelStatus status) {
        return ParcelHistory.builder()
                .parcelId(parcelId)
                .status(status)
                .date(LocalDateTime.now())
                .build();
    }
}
