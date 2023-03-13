package com.kupreychik.parcellapp.mapper;

import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.dto.ParcelDTO;
import com.kupreychik.parcellapp.model.Parcel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Parcel mapper
 */
@Mapper(componentModel = "spring")
public interface ParcelMapper {

    /**
     * Map parcel command to parcel entity
     *
     * @param command create parcel command
     * @return parcel
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address.coordinates", expression = "java(createAddressCommand.getCoordinates().getLatitude() + \", \" + createAddressCommand.getCoordinates().getLongitude())")
    Parcel mapToEntity(CreateParcelCommand command);

    @Mapping(target = "address", ignore = true)
    ParcelDTO mapToDTO(Parcel parcel);
}
