package com.kupreychik.parcellapp.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Create user command. Used to create user
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Schema(description = "Create user command")
public class CreateUserCommand implements Command {

    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Username of user", example = "User 1")
    private String username;

    @Email
    @Schema(description = "Email of user", example = "email@email.com")
    private String email;

    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must contain at least 8 characters, one uppercase letter, " +
                    "one lowercase letter, one number and one special character")
    @Schema(description = "Password of user", example = "password")
    private String password;

    @NotNull
    private CoordinatesCommand coordinates;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", username)
                .append("email", email)
                .append("coordinates", coordinates)
                .toString();
    }
}
