package com.kupreychik.parcellapp.service;

import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.dto.UserDTO;

public interface UserService {

    UserDTO createUser(CreateUserCommand createUserCommand);

    UserDTO createCourier(CreateUserCommand createCourierCommand);
}
