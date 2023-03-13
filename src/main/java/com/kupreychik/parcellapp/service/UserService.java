package com.kupreychik.parcellapp.service;

import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.command.UpdateUserBalanceCommand;
import com.kupreychik.parcellapp.dto.UserBalanceDTO;
import com.kupreychik.parcellapp.dto.UserShortDTO;
import com.kupreychik.parcellapp.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserShortDTO createUser(CreateUserCommand createUserCommand);

    UserShortDTO createCourier(CreateUserCommand createCourierCommand);

    User findUserByUserId(Long userId);

    UserBalanceDTO updateUserBalance(UpdateUserBalanceCommand command);

    UserBalanceDTO getUserBalance(Long userId);

    UserDetails loadUserByUsername(String username);
}
