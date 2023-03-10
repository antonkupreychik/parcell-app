package com.kupreychik.parcellapp.controller;

import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.dto.UiErrorDTO;
import com.kupreychik.parcellapp.dto.UserDTO;
import com.kupreychik.parcellapp.service.UserService;
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

/**
 * User controller
 */
@Slf4j
@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
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
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)))
            },
            tags = {"User"})
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserCommand command) {
        var user = userService.createUser(command);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/courier")
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
                                    schema = @Schema(implementation = UserDTO.class)))
            },
            tags = {"User"})
    public ResponseEntity<UserDTO> createCourier(@Valid @RequestBody  CreateUserCommand command) {
        var courier = userService.createCourier(command);
        return ResponseEntity.ok().body(courier);
    }


}
