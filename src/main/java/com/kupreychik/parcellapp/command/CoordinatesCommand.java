package com.kupreychik.parcellapp.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Coordinates command")
public class CoordinatesCommand implements Command {

    @Schema(description = "Latitude of user/courier", example = "50.4501")
    @Pattern(regexp = "^-?([1-8]?[1-9]|[1-9]0)\\.\\d{1,6}$", message = "Latitude must be in range from -90 to 90")
    private String latitude;

    @Schema(description = "Longitude of user/courier", example = "30.5234")
    @Pattern(regexp = "^-?((1[0-7]|[1-9])?\\d|180)\\.\\d{1,6}$", message = "Longitude must be in range from -180 to 180")
    private String longitude;
}
