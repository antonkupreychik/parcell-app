package com.kupreychik.parcellapp.dto;

import lombok.Data;

@Data
public class ParcelDTO {
    private String name;
    private Double weight;
    private Double width;
    private Double height;
    private Double length;
    private String description;
    private String status;
    private AddressDTO address;
}
