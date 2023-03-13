package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.command.UpdateUserBalanceCommand;
import com.kupreychik.parcellapp.dto.UserBalanceDTO;
import com.kupreychik.parcellapp.dto.UserShortDTO;
import com.kupreychik.parcellapp.enums.OperationType;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
    @Transactional
    public UserShortDTO createUser(CreateUserCommand createUserCommand) {
        try {
            log.info("Creating user with command: {}", createUserCommand);
            checkForEmailAlreadyExist(createUserCommand);
            User user = userMapper.mapToEntity(createUserCommand);
            setRoleForUser(user, ROLE_USER);
            user = userRepository.save(user);
            log.info("User created with id: {}", user.getId());
            return userMapper.mapToDTO(user);
        } catch (Exception e) {
            log.error("Error while creating user. Command: {}", createUserCommand);
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
            setRoleForUser(user, ROLE_COURIER);
            user = userRepository.save(user);
            log.info("Courier created with id: {}", user.getId());
            return userMapper.mapToDTO(user);
        } catch (Exception e) {
            log.error("Error while creating courier with command: {}", createUserCommand, e);
            throw e;
        }
    }

    @Override
    public User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> createParcelException(UiError.USER_NOT_FOUND));
    }

    /**
     * Update user balance. Mehtod available for admin only or for user himself
     *
     * @param command command to update user balance
     * @return updated user balance
     */
    @Override
    @Transactional
    public UserBalanceDTO updateUserBalance(UpdateUserBalanceCommand command) {
        try {
            log.info("Updating user balance with command: {}", command);
            User user = findUserByUserId(command.getUserId());
            OperationType operationType = command.getOperationType();
            if (operationType == OperationType.ADD) {
                increaseBalance(command, user);
            } else if (operationType == OperationType.SUBTRACT) {
                decreaseBalance(command, user);
            } else {
                throw createParcelException(UiError.OPERATION_TYPE_NOT_FOUND);
            }
            user = userRepository.save(user);
            log.info("User balance updated with id: {}", user.getId());
            return userMapper.mapToUserBalanceDTO(user);
        } catch (Exception e) {
            log.error("Error while updating user balance with command: {}", command);
            throw e;
        }
    }

    /**
     * Decrease user balance
     *
     * @param command command to update user balance
     * @param user    user
     */
    private User decreaseBalance(UpdateUserBalanceCommand command, User user) {
        BigDecimal balance = user.getBalance();
        BigDecimal balanceAfterOperation = balance.subtract(command.getAmount());
        if (balanceAfterOperation.compareTo(BigDecimal.ZERO) < 0) {
            throw createParcelException(UiError.BALANCE_IS_NOT_ENOUGH);
        }
        user.setBalance(balanceAfterOperation);
        return user;
    }

    /**
     * Increase user balance
     *
     * @param command command to update user balance
     * @param user    user
     */
    private User increaseBalance(UpdateUserBalanceCommand command, User user) {
        log.info("Adding balance to user with id: {}", user.getId());
        BigDecimal balance = user.getBalance().add(command.getAmount());
        user.setBalance(balance);
        return user;
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
    private void setRoleForUser(User user, RoleName role) {
        user.setRole(roleRepository.findByName(role.name())
                .orElseThrow(() -> createParcelException(UiError.ROLE_NOT_FOUND)));
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
