package com.project.hotels.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateHotelRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String brand;

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
