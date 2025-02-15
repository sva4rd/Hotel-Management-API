package com.project.hotels.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "Response with hotel contacts information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDto {
    @Schema(description = "Hotel phone number", example = "+375 17 309-80-00")
    @NotBlank
    private String phone;

    @Schema(description = "Hotel email", example = "doubletreeminsk.info@hilton.com")
    @Email
    @NotBlank
    private String email;
}
