package com.kupreychik.parcellapp.service;

import com.kupreychik.parcellapp.command.CreateAddressCommand;
import com.kupreychik.parcellapp.dto.AddressDTO;

public interface AddressService {

    AddressDTO saveAddress(CreateAddressCommand createAddressCommand);

    AddressDTO getAddressById(Long id);
}
