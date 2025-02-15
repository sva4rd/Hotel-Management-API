package com.project.hotels.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Schema(description = "Response with hotel address information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    @Schema(description = "Hotel house number", example = "9")
    @NotNull
    private Integer houseNumber;

    @Schema(description = "Hotel street name", example = "Pobediteley Avenue")
    @NotBlank
    private String street;

    @Schema(description = "Hotel city name", example = "Minsk")
    @NotBlank
    private String city;

    @Schema(description = "Hotel country name", example = "Belarus")
    @NotBlank
    private String country;

    @Schema(description = "Hotel postCode", example = "220004")
    @NotBlank
    private String postCode;
}
