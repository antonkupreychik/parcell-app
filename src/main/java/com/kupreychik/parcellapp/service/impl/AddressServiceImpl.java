package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.command.CreateAddressCommand;
import com.kupreychik.parcellapp.dto.AddressDTO;
import com.kupreychik.parcellapp.exception.UiError;
import com.kupreychik.parcellapp.mapper.AddressMapper;
import com.kupreychik.parcellapp.repository.AddressRepository;
import com.kupreychik.parcellapp.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kupreychik.parcellapp.exception.ParcelExceptionUtils.createParcelException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressDTO saveAddress(CreateAddressCommand createAddressCommand) {
        try {
            log.info("Saving address: {}", createAddressCommand);
            var address = addressMapper.mapToEntity(createAddressCommand);
            var savedAddress = addressRepository.save(address);
            log.info("Address saved: {}", savedAddress);
            return addressMapper.mapToDto(savedAddress);
        } catch (Exception e) {
            log.error("Error while saving address: {}", createAddressCommand, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public AddressDTO getAddressById(Long id) {
        try {
            log.info("Getting address by id: {}", id);
            var address = addressRepository.findById(id).orElseThrow(() -> createParcelException(UiError.ADDRESS_NOT_FOUND));
            log.info("Address found: {}", address);
            return addressMapper.mapToDto(address);
        } catch (Exception e) {
            log.error("Error while getting address by id: {}", id, e);
            throw e;
        }
    }
}
