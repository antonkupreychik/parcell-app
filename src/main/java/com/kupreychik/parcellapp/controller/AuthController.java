package com.kupreychik.parcellapp.controller;

import com.kupreychik.parcellapp.command.AuthCommand;
import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.dto.UiErrorDTO;
import com.kupreychik.parcellapp.dto.UserLoginDTO;
import com.kupreychik.parcellapp.dto.UserShortDTO;
import com.kupreychik.parcellapp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(description = "Create user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserCommand.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserShortDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)))
            },
            tags = {"Auth"})
    public ResponseEntity<UserShortDTO> createUser(@Valid @RequestBody CreateUserCommand command) {
        var user = authService.createUser(command);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/register/courier")
    @Operation(description = "Create courier",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserCommand.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Courier created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Courier request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserShortDTO.class)))
            },
            tags = {"Auth"})
    public ResponseEntity<UserShortDTO> createCourier(@Valid @RequestBody CreateUserCommand command) {
        var courier = authService.createCourier(command);
        return ResponseEntity.ok().body(courier);
    }

    @PostMapping("login")
    @Operation(description = "Login",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthCommand.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User logged in",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserLoginDTO.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)))
            },
            tags = {"Auth"})
    public ResponseEntity<UserLoginDTO> login(@RequestBody @Valid AuthCommand request) {
        var user = authService.login(request);
        return ResponseEntity.ok().body(user);
    }
}
