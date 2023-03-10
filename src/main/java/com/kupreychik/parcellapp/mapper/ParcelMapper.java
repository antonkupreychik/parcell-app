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
     * @param createParcelCommand create parcel command
     * @return parcel
     */
    Parcel mapToEntity(CreateParcelCommand createParcelCommand);

    @Mapping(target = "address", ignore = true)
    ParcelDTO mapToDTO(Parcel parcel);
}
