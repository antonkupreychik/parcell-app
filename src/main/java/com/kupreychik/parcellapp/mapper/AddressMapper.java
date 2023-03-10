package com.kupreychik.parcellapp.mapper;

import com.kupreychik.parcellapp.command.CreateAddressCommand;
import com.kupreychik.parcellapp.dto.AddressDTO;
import com.kupreychik.parcellapp.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address mapToEntity(CreateAddressCommand createAddressCommand);


    AddressDTO mapToDto(Address address);
}
