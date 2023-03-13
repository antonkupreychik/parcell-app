package com.kupreychik.parcellapp.service;

import com.kupreychik.parcellapp.command.AuthCommand;
import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.dto.UserLoginDTO;
import com.kupreychik.parcellapp.dto.UserShortDTO;

public interface AuthService {

    UserShortDTO createUser(CreateUserCommand createUserCommand);

    UserShortDTO createCourier(CreateUserCommand createCourierCommand);

    UserLoginDTO login(AuthCommand authCommand);

}
