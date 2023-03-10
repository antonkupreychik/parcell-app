package com.kupreychik.parcellapp.mapper;

import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.model.Parcel;
import org.mapstruct.Mapper;

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
}
