package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.dto.UserDTO;
import com.kupreychik.parcellapp.enums.RoleName;
import com.kupreychik.parcellapp.exception.UiError;
import com.kupreychik.parcellapp.mapper.UserMapper;
import com.kupreychik.parcellapp.model.User;
import com.kupreychik.parcellapp.repository.RoleRepository;
import com.kupreychik.parcellapp.repository.UserRepository;
import com.kupreychik.parcellapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kupreychik.parcellapp.enums.RoleName.ROLE_COURIER;
import static com.kupreychik.parcellapp.enums.RoleName.ROLE_USER;
import static com.kupreychik.parcellapp.exception.ParcelExceptionUtils.createParcelException;

/**
 * User service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Create user
     *
     * @param createUserCommand command to create user
     * @return created user
     */
    @Override
    public UserDTO createUser(CreateUserCommand createUserCommand) {
        try {
            log.info("Creating user with command: {}", createUserCommand);
            User user = userMapper.mapToEntity(createUserCommand);
            setRoleForUser(user, ROLE_USER);
            user = userRepository.save(user);
            log.info("User created with id: {}", user.getId());
            return userMapper.mapToDTO(user);
        } catch (Exception e) {
            log.error("Error while creating user. Command: {}", createUserCommand, e);
            throw e;
        }
    }

    /**
     * Create courier
     *
     * @param createUserCommand command to create courier
     * @return created courier
     */
    @Override
    public UserDTO createCourier(CreateUserCommand createUserCommand) {
        try {
            log.info("Creating courier with command: {}", createUserCommand);
            User user = userMapper.mapToEntity(createUserCommand);
            setRoleForUser(user, ROLE_COURIER);
            user = userRepository.save(user);
            log.info("Courier created with id: {}", user.getId());
            return userMapper.mapToDTO(user);
        } catch (Exception e) {
            log.error("Error while creating courier with command: {}", createUserCommand, e);
            throw e;
        }
    }

    /**
     * Find role by name and set it to user
     *
     * @param user user
     * @param role role
     */
    private void setRoleForUser(User user, RoleName role) {
        user.setRole(roleRepository.findByName(role.name())
                .orElseThrow(() -> createParcelException(UiError.ROLE_NOT_FOUND)));
    }
}
