package com.project.hotels.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "Response with full information about the hotel")
@Getter
@Builder
public class HotelFullDetailsResponse {
    @Schema(description = "Hotel ID", example = "1")
    private Long id;

    @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
    private String name;

    @Schema(description = "Hotel brand", example = "Hilton")
    private String brand;

    private AddressDto address;

    private ContactDto contact;

    private ArrivalTimeDto arrivalTime;

    @Schema(description = "Hotel amenities", example = "[\"Free parking\", \"Free WiFi\", \"Concierge\"]")
    private List<String> amenities;
}
