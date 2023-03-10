package com.kupreychik.parcellapp.command;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Create parcel command. Used to create parcel
 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Create parcel command")
public class CreateParcelCommand implements Command {

    @NotNull
    @Size(min = 3, max = 50)
    @Schema(description = "Name of parcel", example = "Parcel 1")
    private String name;

    @NotNull
    @Min(0)
    @Max(100)
    @Schema(description = "Weight of parcel", example = "10.0")
    private Double weight;

    @NotNull
    @Min(0)
    @Max(100)
    @Schema(description = "Width of parcel", example = "10.0")
    private Double width;

    @NotNull
    @Min(0)
    @Max(100)
    @Schema(description = "Height of parcel", example = "10.0")
    private Double height;

    @NotNull
    @Min(0)
    @Max(100)
    @Schema(description = "Length of parcel", example = "10.0")
    private Double length;

    @NotNull
    @Size(min = 3, max = 50)
    @Schema(description = "Description of parcel", example = "Parcel 1")
    private String description;
}
