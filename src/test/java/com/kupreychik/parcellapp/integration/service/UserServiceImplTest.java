package com.kupreychik.parcellapp.integration.service;

import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.dto.UserDTO;
import com.kupreychik.parcellapp.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayName("Integration UserServiceTest")
class UserServiceImplTest {

    @Autowired
    private UserService userService;


    CreateUserCommand correctCreateUserCommand = CreateUserCommand
            .builder()
            .name("firstName")
            .password("password")
            .email("email@email.com")
            .build();

    UserDTO correctUserDTO = UserDTO
            .builder()
            .id(1L)
            .name("firstName")
            .email("email@email.com")
            .build();

    @Test
    @Transactional
    void should_create_user() {
        var result = userService.createUser(correctCreateUserCommand);

        assertEquals(correctUserDTO, result);
    }

}