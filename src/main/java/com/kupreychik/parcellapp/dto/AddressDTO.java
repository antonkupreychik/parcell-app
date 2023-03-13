package com.kupreychik.parcellapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Address DTO")
public class AddressDTO {
    @Schema(description = "Id of address", example = "1")
    private Long id;
    @Schema(description = "Country of address", example = "Ukraine")
    private String country;
    @Schema(description = "City of address", example = "Kyiv")
    private String city;

    @Schema(description = "Street of address", example = "Khreshchatyk")
    private String street;

    @Schema(description = "House number of address", example = "1")
    private String houseNumber;

    @Schema(description = "Apartment number of address", example = "1")
    private String apartmentNumber;

    @Schema(description = "Zip code of address", example = "01001")
    private String zipCode;

    @Schema(description = "Coordinates of address", example = "50.4501, 30.5234")
    private String coordinates;
}
