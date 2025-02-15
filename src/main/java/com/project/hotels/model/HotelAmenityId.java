package com.project.hotels.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelAmenityId implements Serializable {
    private Long hotelId;
    private Long amenityId;
}

