package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.command.AuthCommand;
import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.dto.UserLoginDTO;
import com.kupreychik.parcellapp.dto.UserShortDTO;
import com.kupreychik.parcellapp.enums.RoleName;
import com.kupreychik.parcellapp.exception.UiError;
import com.kupreychik.parcellapp.security.JwtTokenProvider;
import com.kupreychik.parcellapp.mapper.UserMapper;
import com.kupreychik.parcellapp.model.User;
import com.kupreychik.parcellapp.repository.RoleRepository;
import com.kupreychik.parcellapp.repository.UserRepository;
import com.kupreychik.parcellapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kupreychik.parcellapp.exception.ParcelExceptionUtils.createParcelException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * Create user
     *
     * @param createUserCommand command to create user
     * @return created user
     */
    @Override
    @Transactional
    public UserShortDTO createUser(CreateUserCommand createUserCommand) {
        try {
            log.info("Creating user with command: {}", createUserCommand);
            checkForEmailAlreadyExist(createUserCommand);
            User user = userMapper.mapToEntity(createUserCommand);
            addRoleAndEncryptPasswordForUser(user, RoleName.ROLE_USER);
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
    @Transactional
    public UserShortDTO createCourier(CreateUserCommand createUserCommand) {
        try {
            log.info("Creating courier with command: {}", createUserCommand);
            checkForEmailAlreadyExist(createUserCommand);
            User user = userMapper.mapToEntity(createUserCommand);
            addRoleAndEncryptPasswordForUser(user, RoleName.ROLE_COURIER);
            user = userRepository.save(user);
            log.info("Courier created with id: {}", user.getId());
            return userMapper.mapToDTO(user);
        } catch (Exception e) {
            log.error("Error while creating courier with command: {}", createUserCommand, e);
            throw e;
        }
    }

    private void addRoleAndEncryptPasswordForUser(User user, String role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        setRoleForUser(user, role);
    }

    /**
     * Login user
     *
     * @return user login dto
     */
    @Override
    public UserLoginDTO login(AuthCommand authCommand) {
        try {
            var user = userRepository.findByUsername(authCommand.getUsername())
                    .orElseThrow(() -> createParcelException(UiError.USERNAME_OR_PASSWORD_INCORRECT));
            matchPassword(authCommand, user);
            log.info("Login attempt for user: {}", authCommand.getUsername());
            var token = jwtTokenProvider.generateToken(authCommand, user);
            log.info("Login successful for user: {}", authCommand.getUsername());
            return UserLoginDTO.builder()
                    .username(authCommand.getUsername())
                    .token(token)
                    .build();
        } catch (Exception ex) {
            log.warn("Login failed for user: {}", authCommand.getUsername());
            throw createParcelException(UiError.USERNAME_OR_PASSWORD_INCORRECT);
        }
    }

    /**
     * Match password from command with password from user
     *
     * @param authCommand command to auth
     * @param user       user
     */
    private void matchPassword(AuthCommand authCommand, User user) {
        if (!passwordEncoder.matches(authCommand.getPassword(), user.getPassword())) {
            throw createParcelException(UiError.USERNAME_OR_PASSWORD_INCORRECT);
        }
    }

    /**
     * Check if email already exists. If exist throw exception
     *
     * @param createUserCommand command to create user
     */
    private void checkForEmailAlreadyExist(CreateUserCommand createUserCommand) {
        if (isEmailExist(createUserCommand.getEmail())) {
            throw createParcelException(UiError.EMAIL_ALREADY_EXIST);
        }
    }

    /**
     * Find a role by name and set it to user
     *
     * @param user user
     * @param role role
     */
    private void setRoleForUser(User user, String role) {
        roleRepository.findByAuthority(role).ifPresentOrElse(
                user::setRole,
                () -> {
                    throw createParcelException(UiError.ROLE_NOT_FOUND);
                });
    }

    /**
     * Check if email already exists
     *
     * @param email email
     * @return true, if email already exists
     */
    private boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
