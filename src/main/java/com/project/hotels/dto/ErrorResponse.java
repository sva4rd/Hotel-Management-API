package com.project.hotels.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "Error response")
@AllArgsConstructor
@Getter
public class ErrorResponse {
    @Schema(description = "Error message", example = "Error: Invalid parameter")
    private String error;
}
