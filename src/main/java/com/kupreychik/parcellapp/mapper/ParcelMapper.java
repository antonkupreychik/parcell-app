package com.kupreychik.parcellapp.mapper;

import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.dto.AddressDTO;
import com.kupreychik.parcellapp.dto.ParcelDTO;
import com.kupreychik.parcellapp.model.Address;
import com.kupreychik.parcellapp.model.Parcel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Parcel mapper
 */
@Mapper(componentModel = "spring", imports = AddressMapper.class)
public interface ParcelMapper {

    /**
     * Map parcel command to parcel entity
     *
     * @param command create parcel command
     * @return parcel
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.kupreychik.parcellapp.enums.ParcelStatus.NEW)")
    @Mapping(target = "address.coordinates", expression = "java(createAddressCommand.getCoordinates().getLatitude() + \", \" + createAddressCommand.getCoordinates().getLongitude())")
    Parcel mapToEntity(CreateParcelCommand command);

    /**
     * Map to DTO, use custom mapping for address
     *
     * @param parcel parcel
     * @return parcel DTO
     */
    @Mapping(target = "address", source = "address", qualifiedByName = "mapAddressToDTO")
    ParcelDTO mapToDTO(Parcel parcel);

    /**
     * Custom mapping for address
     *
     * @param address address
     * @return address DTO
     */
    @Named("mapAddressToDTO")
    static AddressDTO mapAddressToDTO(Address address) {
        return Mappers.getMapper(AddressMapper.class).mapToDto(address);
    }
}
