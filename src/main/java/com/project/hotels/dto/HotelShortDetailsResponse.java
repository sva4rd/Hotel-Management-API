package com.project.hotels.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Response with brief information about the hotel")
@Getter
@Builder
public class HotelShortDetailsResponse {
    @Schema(description = "Hotel ID", example = "1")
    private Long id;

    @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
    private String name;

    @Schema(description = "Hotel description", example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...")
    private String description;

    @Schema(description = "Hotel address", example = "9 Pobediteley Avenue, Minsk, 220004, Belarus")
    private String address;

    @Schema(description = "Hotel phone", example = "+375 17 309-80-00")
    private String phone;
}
