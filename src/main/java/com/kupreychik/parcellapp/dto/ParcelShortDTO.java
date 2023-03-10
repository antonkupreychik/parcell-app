package com.kupreychik.parcellapp.dto;

import com.kupreychik.parcellapp.enums.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParcelShortDTO {
    private Long id;
    private String name;
    private Double weight;
    private Double width;
    private Double height;
    private Double length;
    private String description;
    private ParcelStatus status;
}
