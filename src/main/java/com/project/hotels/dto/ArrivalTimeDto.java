package com.project.hotels.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Response with hotel arrival time information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrivalTimeDto {
    @Schema(description = "Check In time", example = "14:00")
    private String checkIn;

    @Schema(description = "Check Out time", example = "12:00")
    private String checkOut;
}
