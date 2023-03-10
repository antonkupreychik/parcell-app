package com.kupreychik.parcellapp.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Create address command")
public class CreateAddressCommand implements Command {

    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Country of address", example = "Ukraine")
    private String country;

    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "City of address", example = "Kyiv")
    private String city;

    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Street of address", example = "Khreshchatyk")
    private String street;

    @NotNull
    @Pattern(regexp = "^[0-9]*$")
    @Schema(description = "House number of address", example = "1")
    private String houseNumber;

    @NotNull
    @Pattern(regexp = "^[0-9]*$")
    @Schema(description = "Apartment number of address", example = "1")
    private String apartmentNumber;

    @NotNull
    @Pattern(regexp = "^[0-9]*$")
    @Schema(description = "Zip code of address", example = "01001")
    private String zipCode;

    @NotNull
    @Schema(description = "Coordinates of parcel destination")
    private CoordinatesCommand coordinates;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("country", country)
                .append("city", city)
                .append("street", street)
                .append("houseNumber", houseNumber)
                .append("apartmentNumber", apartmentNumber)
                .append("zipCode", zipCode)
                .append("coordinates", coordinates)
                .toString();
    }
}
