package com.kupreychik.parcellapp.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Auth command")
public class AuthCommand {

    @NotNull
    @Schema(description = "Username", example = "admin")
    private String username;

    @NotNull
    private String password;
}
