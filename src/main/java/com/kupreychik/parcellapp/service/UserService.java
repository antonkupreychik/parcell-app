package com.kupreychik.parcellapp.service;

import com.kupreychik.parcellapp.command.UpdateUserBalanceCommand;
import com.kupreychik.parcellapp.dto.UserBalanceDTO;
import com.kupreychik.parcellapp.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

public interface UserService {
    User findUserByUserId(Long userId);

    User findUserByPrincipal(Principal principal);

    UserBalanceDTO updateUserBalance(UpdateUserBalanceCommand command);

    UserBalanceDTO getUserBalance(Long userId, Principal principal);

    UserDetails loadUserByUsername(String username);
}
