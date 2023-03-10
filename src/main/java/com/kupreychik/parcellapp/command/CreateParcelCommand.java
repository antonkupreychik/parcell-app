package com.kupreychik.parcellapp.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Create parcel command. Used to create parcel
 */
@Getter
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
    @Max(1000)
    @Schema(description = "Weight of parcel", example = "10.0")
    private Double weight;

    @NotNull
    @Min(0)
    @Max(1000)
    @Schema(description = "Width of parcel", example = "10.0")
    private Double width;

    @NotNull
    @Min(0)
    @Max(1000)
    @Schema(description = "Height of parcel", example = "10.0")
    private Double height;

    @NotNull
    @Min(0)
    @Max(1000)
    @Schema(description = "Length of parcel", example = "10.0")
    private Double length;

    @NotNull
    @Size(min = 3, max = 50)
    @Schema(description = "Description of parcel", example = "Parcel 1")
    private String description;

    @NotNull
    @Schema(description = "Address of parcel destination")
    private CreateAddressCommand address;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name)
                .append("weight", weight)
                .append("width", width)
                .append("height", height)
                .append("length", length)
                .append("description", description)
                .append("address", address)
                .toString();
    }
}
