package com.kupreychik.parcellapp.controller;

import com.kupreychik.parcellapp.command.CreateAddressCommand;
import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.dto.PageDTO;
import com.kupreychik.parcellapp.dto.ParcelDTO;
import com.kupreychik.parcellapp.dto.ParcelShortDTO;
import com.kupreychik.parcellapp.dto.UiErrorDTO;
import com.kupreychik.parcellapp.enums.ParcelStatus;
import com.kupreychik.parcellapp.service.ParcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/v1/parcels")
@RestController
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;

    //user
    @PostMapping
    @Operation(description = "Create parcel",
            summary = "Create parcel",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create parcel command",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateParcelCommand.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parcel created",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParcelDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)
                            )
                    )
            },
            tags = {"Parcel"})
    public ResponseEntity<ParcelDTO> createParcel(@Valid @RequestBody CreateParcelCommand parcelCommand, Long userId) {
        var parcel = parcelService.createParcel(parcelCommand, userId);
        return ResponseEntity.ok().body(parcel);
    }

    //user
    @GetMapping
    @Operation(description = "Get my parcels",
            summary = "Get my parcels",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parcel found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParcelShortDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)
                            )
                    )
            },
            tags = {"Parcel"})
    public ResponseEntity<PageDTO<ParcelShortDTO>> getMyParcels(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                                @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                                                @RequestParam(value = "sort", required = false, defaultValue = "name") String sortField,
                                                                @RequestParam(value = "direction", required = false, defaultValue = "ASC") Sort.Direction direction,
                                                                @RequestParam(value = "userId") Long userId) {
        Pageable pageable = PageRequest.of(page, size, direction, sortField);
        var parcels = parcelService.getMyParcels(userId, search, pageable);
        return ResponseEntity.ok().body(parcels);
    }

    //all
    @GetMapping("/status")
    @Operation(description = "Get parcels by status and user id",
            summary = "Get parcels by status and user id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parcel found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParcelShortDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)
                            )
                    )
            },
            tags = {"Parcel"})
    public ResponseEntity<PageDTO<ParcelShortDTO>> getParcelsByStatus(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                                      @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                                                      @RequestParam(value = "sort", required = false, defaultValue = "name") String sortField,
                                                                      @RequestParam(value = "direction", required = false, defaultValue = "ASC") Sort.Direction direction,
                                                                      @RequestParam(value = "userId") Long userId,
                                                                      @RequestParam(value = "status") ParcelStatus status) {
        Pageable pageable = PageRequest.of(page, size, direction, sortField);
        var parcels = parcelService.getMyParcelsByStatus(status, userId, search, pageable);
        return ResponseEntity.ok().body(parcels);
    }

    //all
    @GetMapping("/{id}")
    @Operation(description = "Get parcel by id",
            summary = "Get parcel by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parcel found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParcelDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)
                            )
                    )
            },
            tags = {"Parcel"})
    public ResponseEntity<ParcelDTO> getParcelById(@PathVariable Long id,
                                                   @RequestParam(value = "userId") Long userId) {
        var parcel = parcelService.getParcelById(userId, id);
        return ResponseEntity.ok().body(parcel);
    }

    //only for users
    @PostMapping("/{id}/address")
    @Operation(description = "Change parcel address",
            summary = "Change parcel address",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Change parcel address command",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateAddressCommand.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parcel address changed",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParcelDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)
                            )
                    )
            },
            tags = {"Parcel"})
    public ResponseEntity<ParcelDTO> updateParcelAddress(@PathVariable Long id,
                                                         @Valid @RequestBody CreateAddressCommand command,
                                                         @RequestParam(value = "userId") Long userId) {
        var parcel = parcelService.updateParcelAddress(userId, id, command);
        return ResponseEntity.ok().body(parcel);
    }

    //only user admin
    @PostMapping("/{id}/cancel")
    @Operation(description = "Cancel parcel",
            summary = "Cancel parcel",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parcel canceled",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParcelDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)
                            )
                    )
            },
            tags = {"Parcel"})
    public ResponseEntity<ParcelDTO> cancelParcel(@PathVariable Long id,
                                                  @RequestParam(value = "userId") Long userId) {
        parcelService.cancelParcel(userId, id);
        return ResponseEntity.noContent().build();
    }

    //only admin method
    @PostMapping("/{parcelId}/assign/{courierId}}")
    @Operation(description = "Assign parcel to courier",
            summary = "Assign parcel to courier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parcel assigned",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParcelDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)
                            )
                    )
            },
            tags = {"Parcel"})
    public ResponseEntity<ParcelDTO> assignParcel(@PathVariable Long parcelId,
                                                  @PathVariable Long courierId) {
        var parcel = parcelService.assignCourier(parcelId, courierId);
        return ResponseEntity.ok().body(parcel);
    }

    //only admin and courier method
    @PostMapping("/{parcelId}/status/{status}")
    @Operation(description = "Change parcel status",
            summary = "Change parcel status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parcel status changed",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParcelDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UiErrorDTO.class)
                            )
                    )
            },
            tags = {"Parcel"})
    public ResponseEntity<ParcelDTO> changeParcelStatus(@PathVariable Long parcelId,
                                                        @PathVariable ParcelStatus status) {
        var parcel = parcelService.changeParcelStatus(parcelId, status);
        return ResponseEntity.ok().body(parcel);
    }


}
