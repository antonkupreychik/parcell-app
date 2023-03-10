package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.command.UpdateUserBalanceCommand;
import com.kupreychik.parcellapp.dto.UserBalanceDTO;
import com.kupreychik.parcellapp.enums.OperationType;
import com.kupreychik.parcellapp.enums.RoleName;
import com.kupreychik.parcellapp.exception.UiError;
import com.kupreychik.parcellapp.mapper.UserMapper;
import com.kupreychik.parcellapp.model.User;
import com.kupreychik.parcellapp.repository.UserRepository;
import com.kupreychik.parcellapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;

import static com.kupreychik.parcellapp.exception.ParcelExceptionUtils.createParcelException;
import static java.lang.String.format;
import static java.util.Objects.isNull;

/**
 * User service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> createParcelException(UiError.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public User findUserByPrincipal(Principal principal) {
        return userRepository.findByUsername(principal.getName())
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
     * Method available for admin only or for user himself
     *
     * @param userId    user id
     * @param principal principal
     * @return user balance
     */
    @Override
    @Transactional
    public UserBalanceDTO getUserBalance(Long userId, Principal principal) {
        try {
            User userWhoMakesRequest = findUserByPrincipal(principal);
            if (isNull(userId)) {
                return userMapper.mapToUserBalanceDTO(userWhoMakesRequest);
            } else if (userWhoMakesRequest.getRole().getAuthority().equals(RoleName.ROLE_ADMIN)) {
                User user = findUserByUserId(userId);
                return userMapper.mapToUserBalanceDTO(user);
            } else {
                throw createParcelException(UiError.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error while getting user balance with id: {}", userId);
            throw e;
        }
    }

    /**
     * Decrease user balance
     *
     * @param command command to update user balance
     * @param user    user
     */
    private void decreaseBalance(UpdateUserBalanceCommand command, User user) {
        BigDecimal balance = user.getBalance();
        BigDecimal balanceAfterOperation = balance.subtract(command.getAmount());
        if (balanceAfterOperation.compareTo(BigDecimal.ZERO) < 0) {
            throw createParcelException(UiError.BALANCE_IS_NOT_ENOUGH);
        }
        user.setBalance(balanceAfterOperation);
    }

    /**
     * Increase user balance
     *
     * @param command command to update user balance
     * @param user    user
     */
    private void increaseBalance(UpdateUserBalanceCommand command, User user) {
        log.info("Adding balance to user with id: {}", user.getId());
        BigDecimal balance = user.getBalance().add(command.getAmount());
        user.setBalance(balance);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () ->
                                new UsernameNotFoundException(
                                        format("User with username - %s, not found", username)));
    }


}
