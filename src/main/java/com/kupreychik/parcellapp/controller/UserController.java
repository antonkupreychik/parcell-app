package com.kupreychik.parcellapp.controller;

import com.kupreychik.parcellapp.command.CreateUserCommand;
import com.kupreychik.parcellapp.command.UpdateUserBalanceCommand;
import com.kupreychik.parcellapp.dto.UiErrorDTO;
import com.kupreychik.parcellapp.dto.UserBalanceDTO;
import com.kupreychik.parcellapp.dto.UserShortDTO;
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
                                    schema = @Schema(implementation = UserShortDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)))
            },
            tags = {"User"})
    public ResponseEntity<UserShortDTO> createUser(@Valid @RequestBody CreateUserCommand command) {
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
                                    schema = @Schema(implementation = UserShortDTO.class)))
            },
            tags = {"User"})
    public ResponseEntity<UserShortDTO> createCourier(@Valid @RequestBody CreateUserCommand command) {
        var courier = userService.createCourier(command);
        return ResponseEntity.ok().body(courier);
    }

    //only for admin and user himself
    @PostMapping("/balance")
    @Operation(description = "Update user balance",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUserBalanceCommand.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User balance updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserBalanceDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)))
            },
            tags = {"User"})
    public ResponseEntity<UserBalanceDTO> updateUserBalance(@Valid @RequestBody UpdateUserBalanceCommand command) {
        var userBalance = userService.updateUserBalance(command);
        return ResponseEntity.ok().body(userBalance);
    }


}
