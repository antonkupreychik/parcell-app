package com.kupreychik.parcellapp.mapper;

import com.kupreychik.parcellapp.command.CreateAddressCommand;
import com.kupreychik.parcellapp.dto.AddressDTO;
import com.kupreychik.parcellapp.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "coordinates", expression = "java(createAddressCommand.getCoordinates().getLatitude() + \", \" + createAddressCommand.getCoordinates().getLongitude())")
    Address mapToEntity(CreateAddressCommand createAddressCommand);


    AddressDTO mapToDto(Address address);
}
