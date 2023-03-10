package com.kupreychik.parcellapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "User DTO")
public class UserShortDTO {

    @Schema(description = "Id of user", example = "1")
    private Long id;
    @Schema(description = "Username of user", example = "User 1")
    private String username;
    @Schema(description = "Email of user", example = "email@email.com")
    private String email;
}
