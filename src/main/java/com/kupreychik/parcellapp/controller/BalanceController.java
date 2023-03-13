package com.kupreychik.parcellapp.controller;

import com.kupreychik.parcellapp.command.UpdateUserBalanceCommand;
import com.kupreychik.parcellapp.dto.UiErrorDTO;
import com.kupreychik.parcellapp.dto.UserBalanceDTO;
import com.kupreychik.parcellapp.enums.RoleName;
import com.kupreychik.parcellapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

/**
 * User controller
 */
@Slf4j
@RequestMapping("/api/v1/balance")
@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final UserService userService;

    @RolesAllowed({RoleName.ROLE_USER, RoleName.ROLE_ADMIN})
    @PostMapping
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
            tags = {"Balance"})
    public ResponseEntity<UserBalanceDTO> updateUserBalance(@Valid @RequestBody UpdateUserBalanceCommand command) {
        var userBalance = userService.updateUserBalance(command);
        return ResponseEntity.ok().body(userBalance);
    }

    @RolesAllowed({RoleName.ROLE_USER, RoleName.ROLE_ADMIN, RoleName.ROLE_COURIER})
    @GetMapping
    @Operation(description = "Get user balance",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User balance",
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
            tags = {"Balance"})
    public ResponseEntity<UserBalanceDTO> getUserBalance(Long userId) {
        var userBalance = userService.getUserBalance(userId);
        return ResponseEntity.ok().body(userBalance);
    }
}
