package com.project.hotels.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HotelFullDetailsResponse {
    private Long id;
    private String name;
    private String brand;
    private AddressDto address;
    private ContactDto contact;
    private ArrivalTimeDto arrivalTime;
    private List<String> amenities;
}
