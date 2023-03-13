package com.kupreychik.parcellapp.service;

import com.kupreychik.parcellapp.command.CreateAddressCommand;
import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.dto.PageDTO;
import com.kupreychik.parcellapp.dto.ParcelDTO;
import com.kupreychik.parcellapp.dto.ParcelShortDTO;
import com.kupreychik.parcellapp.enums.ParcelStatus;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface ParcelService {

    ParcelDTO createParcel(CreateParcelCommand createParcelCommand, Principal principal);

    ParcelDTO getParcelById(Long parcelId, Principal principal);

    PageDTO<ParcelShortDTO> getMyParcels(Long userId, ParcelStatus[] statuses, String search, Pageable pageable, Principal principal);

    ParcelDTO updateParcelAddress(Long parcelId, CreateAddressCommand createAddressCommand, Principal principal);

    void cancelParcel(Long parcelId, Principal principal);

    ParcelDTO assignCourier(Long parcelId, Long courierId);

    ParcelDTO changeParcelStatus(Long parcelId, ParcelStatus status, Principal principal);

    PageDTO<ParcelShortDTO> getAllParcels(Pageable pageable);
}
