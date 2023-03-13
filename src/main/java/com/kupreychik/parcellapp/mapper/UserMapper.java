package com.kupreychik.parcellapp.mapper;

import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.dto.UserBalanceDTO;
import com.kupreychik.parcellapp.dto.UserShortDTO;
import com.kupreychik.parcellapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User mapper
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Map user command to user entity
     *
     * @param createUserCommand create user command
     * @return user
     */
    @Mapping(target = "coordinates", expression = "java(createUserCommand.getCoordinates().getLatitude() + \",\" + createUserCommand.getCoordinates().getLongitude())")
    @Mapping(target = "balance", constant = "0")
    @Mapping(target = "id", ignore = true)
    User mapToEntity(CreateUserCommand createUserCommand);

    /**
     * Map user entity to user DTO
     *
     * @param user user
     * @return user DTO
     */
    UserShortDTO mapToDTO(User user);

    UserBalanceDTO mapToUserBalanceDTO(User user);
}
