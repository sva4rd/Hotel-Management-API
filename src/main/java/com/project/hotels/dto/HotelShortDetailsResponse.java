package com.project.hotels.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotelShortDetailsResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
