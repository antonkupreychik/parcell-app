package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.annotation.UnitTest;
import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.exception.ParcelException;
import com.kupreychik.parcellapp.mapper.UserMapper;
import com.kupreychik.parcellapp.model.Role;
import com.kupreychik.parcellapp.model.User;
import com.kupreychik.parcellapp.repository.RoleRepository;
import com.kupreychik.parcellapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@DisplayName("Unit UserServiceTest")
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    CreateUserCommand correctCreateUserCommand = CreateUserCommand
            .builder()
            .username("firstName")
            .password("password")
            .email("email@email.com")
            .build();


    @Test
    void should_create_user() {
        when(roleRepository.findByAuthority(any(String.class))).thenReturn(java.util.Optional.of(new Role()));
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userMapper.mapToEntity(any(CreateUserCommand.class))).thenReturn(new User());

        userService.createUser(correctCreateUserCommand);

        verify(userRepository, times(1)).save(any(User.class));
        verify(roleRepository, times(1)).findByAuthority(any(String.class));
    }

    @Test
    void should_throw_exception_when_role_not_found() {
        when(roleRepository.findByAuthority(any(String.class))).thenReturn(java.util.Optional.empty());

        assertThrows(ParcelException.class, () -> userService.createUser(correctCreateUserCommand));

        verify(userRepository, times(0)).save(any(User.class));
        verify(roleRepository, times(1)).findByAuthority(any(String.class));
    }

    @Test
    void should_create_courier() {
        when(roleRepository.findByAuthority(any(String.class))).thenReturn(java.util.Optional.of(new Role()));
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userMapper.mapToEntity(any(CreateUserCommand.class))).thenReturn(new User());

        userService.createCourier(correctCreateUserCommand);

        verify(userRepository, times(1)).save(any(User.class));
        verify(roleRepository, times(1)).findByAuthority(any(String.class));
    }

    @Test
    void should_throw_exception_when_role_not_found_for_courier() {
        when(roleRepository.findByAuthority(any(String.class))).thenReturn(java.util.Optional.empty());

        assertThrows(ParcelException.class, () -> userService.createCourier(correctCreateUserCommand));

        verify(userRepository, times(0)).save(any(User.class));
        verify(roleRepository, times(1)).findByAuthority(any(String.class));
    }
}