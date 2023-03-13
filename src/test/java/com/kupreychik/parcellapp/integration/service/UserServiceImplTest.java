package com.kupreychik.parcellapp.integration.service;

import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.dto.UserShortDTO;
import com.kupreychik.parcellapp.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayName("Integration UserServiceTest")
class UserServiceImplTest {

    @Autowired
    private UserService userService;


    CreateUserCommand correctCreateUserCommand = CreateUserCommand
            .builder()
            .username("firstName")
            .password("password")
            .email("email@email.com")
            .build();

    UserShortDTO correctUserShortDTO = UserShortDTO
            .builder()
            .id(1L)
            .username("firstName")
            .email("email@email.com")
            .build();

    @Test
    @Transactional
    void should_create_user() {

    }

}