package com.kupreychik.parcellapp.controller;

import com.kupreychik.parcellapp.command.CreateAddressCommand;
import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.dto.PageDTO;
import com.kupreychik.parcellapp.dto.ParcelDTO;
import com.kupreychik.parcellapp.dto.ParcelShortDTO;
import com.kupreychik.parcellapp.dto.UiErrorDTO;
import com.kupreychik.parcellapp.enums.ParcelStatus;
import com.kupreychik.parcellapp.enums.RoleName;
import com.kupreychik.parcellapp.service.ParcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RequestMapping("/api/v1/parcels")
@RestController
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;

    @RolesAllowed(RoleName.ROLE_USER)
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
    public ResponseEntity<ParcelDTO> createParcel(@Valid @RequestBody CreateParcelCommand parcelCommand, Principal principal) {
        var parcel = parcelService.createParcel(parcelCommand, principal);
        return ResponseEntity.ok().body(parcel);
    }


    @RolesAllowed({RoleName.ROLE_USER, RoleName.ROLE_COURIER, RoleName.ROLE_ADMIN})
    @GetMapping
    @Operation(description = "Get parcels by status and user id",
            summary = "Get parcels by status and user id",
            parameters = {
                    @Parameter(name = "statuses", description = "Parcel statuses", required = true)
            },
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
                                                                      @RequestParam(value = "statuses") ParcelStatus[] statuses,
                                                                      Principal principal) {
        Pageable pageable = PageRequest.of(page, size, direction, sortField);
        var parcels = parcelService.getMyParcels(userId, statuses, search, pageable, principal);
        return ResponseEntity.ok().body(parcels);
    }

    @RolesAllowed({RoleName.ROLE_USER, RoleName.ROLE_COURIER, RoleName.ROLE_ADMIN})
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
                                                   Principal principal) {
        var parcel = parcelService.getParcelById(id, principal);
        return ResponseEntity.ok().body(parcel);
    }

    @RolesAllowed(RoleName.ROLE_USER)
    @PostMapping("/{parcelId}/address")
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
    public ResponseEntity<ParcelDTO> updateParcelAddress(@PathVariable Long parcelId,
                                                         @Valid @RequestBody CreateAddressCommand command,
                                                         Principal principal) {
        var parcel = parcelService.updateParcelAddress(parcelId, command, principal);
        return ResponseEntity.ok().body(parcel);
    }

    @RolesAllowed({RoleName.ROLE_USER, RoleName.ROLE_ADMIN})
    @PostMapping("/{parcelId}/cancel")
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
    public ResponseEntity<ParcelDTO> cancelParcel(@PathVariable Long parcelId,
                                                  Principal principal) {
        parcelService.cancelParcel(parcelId, principal);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed(RoleName.ROLE_ADMIN)
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

    @RolesAllowed({RoleName.ROLE_ADMIN, RoleName.ROLE_COURIER})
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
                                                        @PathVariable ParcelStatus status, Principal principal) {
        var parcel = parcelService.changeParcelStatus(parcelId, status, principal);
        return ResponseEntity.ok().body(parcel);
    }
}
