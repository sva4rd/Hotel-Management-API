package com.project.hotels.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Schema(description = "Request with hotel information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateHotelRequest {
    @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
    @NotBlank
    private String name;

    @Schema(description = "Hotel brand", example = "Hilton")
    @NotBlank
    private String brand;

    @Schema(description = "Hotel description", example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...")
    @NotBlank
    private String description;

    @Valid
    @NotNull
    private AddressDto address;

    @Valid
    @NotNull
    private ContactDto contact;

    @Valid
    @NotNull
    private ArrivalTimeDto arrivalTime;
}
