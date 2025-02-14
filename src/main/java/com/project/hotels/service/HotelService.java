package com.project.hotels.service;

import com.project.hotels.dto.*;
import com.project.hotels.model.*;
import com.project.hotels.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final FormatService formatService;
    private final SaveService saveService;

    public HotelService(HotelRepository hotelRepository, FormatService formatService,
                        SaveService saveService) {
        this.hotelRepository = hotelRepository;
        this.formatService = formatService;
        this.saveService = saveService;
    }

    public List<HotelShortDetailsResponse> getAllHotels() {
        return hotelRepository.findAllWithShortDetails().stream().map(formatService::mapToShortDto).collect(Collectors.toList());
    }

    public HotelFullDetailsResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findByIdWithFullDetails(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));

        return formatService.mapToFullDto(hotel);
    }

    public List<HotelShortDetailsResponse> searchHotels(String name, String brand, String city, String country, List<String> amenities) {
        Specification<Hotel> spec = HotelSpecification.byFilters(name, brand, city, country, amenities);
        List<Hotel> hotels = hotelRepository.findAll(spec);

        return hotels.stream()
                .map(formatService::mapToShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public HotelShortDetailsResponse createHotel(CreateHotelRequest request) {
        Hotel hotel = saveService.saveHotel(request);
        return formatService.mapToShortDto(hotel);
    }

    public void addAmenitiesToHotel(Long hotelId, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id: " + hotelId));

        saveService.saveAmenities(hotel, amenities);
    }

}
