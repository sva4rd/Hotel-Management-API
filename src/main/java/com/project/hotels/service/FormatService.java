package com.project.hotels.service;

import com.project.hotels.dto.*;
import com.project.hotels.model.Address;
import com.project.hotels.model.Hotel;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class FormatService {
    HotelShortDetailsResponse mapToShortDto(Hotel hotel) {
        return HotelShortDetailsResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(formatAddressToString(hotel))
                .phone(hotel.getContact() != null ? hotel.getContact().getPhone() : null)
                .build();
    }

    HotelFullDetailsResponse mapToFullDto(Hotel hotel) {
        return HotelFullDetailsResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .brand(hotel.getBrand())
                .address(formatAddress(hotel))
                .contact(formatContact(hotel))
                .arrivalTime(formatArrivalTime(hotel))
                .amenities(hotel.getHotelAmenities().stream()
                        .map(ha -> ha.getAmenity().getName())
                        .collect(Collectors.toList()))
                .build();
    }

    private String formatAddressToString(Hotel hotel) {
        Address address = hotel.getAddress();
        if (address== null)
            return "Unknown Address";

        return String.format("%d %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity().getName(),
                address.getPostCode(),
                address.getCity().getCountry().getName());
    }

    private AddressDto formatAddress(Hotel hotel) {
        if (hotel.getAddress() == null || hotel.getAddress().getCity() == null) {
            return null;
        }
        return AddressDto.builder()
                .houseNumber(hotel.getAddress().getHouseNumber())
                .street(hotel.getAddress().getStreet())
                .city(hotel.getAddress().getCity().getName())
                .country(hotel.getAddress().getCity().getCountry().getName())
                .postCode(hotel.getAddress().getPostCode())
                .build();
    }

    private ContactDto formatContact(Hotel hotel) {
        if (hotel.getContact() == null) {
            return null;
        }
        return ContactDto.builder()
                .phone(hotel.getContact().getPhone())
                .email(hotel.getContact().getEmail())
                .build();
    }

    private ArrivalTimeDto formatArrivalTime(Hotel hotel) {
        if (hotel.getArrivalTime() == null) {
            return null;
        }
        return ArrivalTimeDto.builder()
                .checkIn(hotel.getArrivalTime().getCheckIn().toString())
                .checkOut(hotel.getArrivalTime().getCheckOut().toString())
                .build();
    }
}
