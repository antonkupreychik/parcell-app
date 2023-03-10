package com.kupreychik.parcellapp.service;

import com.kupreychik.parcellapp.command.CreateAddressCommand;
import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.dto.PageDTO;
import com.kupreychik.parcellapp.dto.ParcelDTO;
import com.kupreychik.parcellapp.dto.ParcelShortDTO;
import com.kupreychik.parcellapp.enums.ParcelStatus;
import org.springframework.data.domain.Pageable;

public interface ParcelService {

    ParcelDTO createParcel(CreateParcelCommand createParcelCommand, Long userId);

    ParcelDTO getParcelById(Long userId, Long parcelId);

    PageDTO<ParcelShortDTO> getMyParcels(Long userId, String search, Pageable pageable);

    PageDTO<ParcelShortDTO> getMyParcelsByStatus(ParcelStatus status, Long userId, String search, Pageable pageable);

    ParcelDTO updateParcelAddress(Long userId, Long parcelId, CreateAddressCommand createAddressCommand);

    void cancelParcel(Long userId, Long parcelId);

    ParcelDTO assignCourier(Long parcelId, Long courierId);

    ParcelDTO changeParcelStatus(Long parcelId, ParcelStatus status);

    PageDTO<ParcelShortDTO> getAllParcels(Pageable pageable);
}
