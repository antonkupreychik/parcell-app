package com.kupreychik.parcellapp.mapper;

import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.dto.UserDTO;
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
    @Mapping(target = "id", ignore = true)
    User mapToEntity(CreateUserCommand createUserCommand);

    /**
     * Map user entity to user DTO
     *
     * @param user user
     * @return user DTO
     */
    UserDTO mapToDTO(User user);
}
