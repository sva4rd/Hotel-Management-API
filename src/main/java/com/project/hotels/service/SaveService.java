package com.project.hotels.service;

import com.project.hotels.dto.*;
import com.project.hotels.model.*;
import com.project.hotels.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaveService {
    private final HotelRepository hotelRepository;
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final ContactRepository contactsRepository;
    private final ArrivalTimeRepository arrivalTimeRepository;
    private final AmenityRepository amenityRepository;
    private final HotelAmenityRepository hotelAmenityRepository;

    public SaveService(HotelRepository hotelRepository, AddressRepository addressRepository,
                       CityRepository cityRepository, CountryRepository countryRepository,
                       ContactRepository contactsRepository, ArrivalTimeRepository arrivalTimeRepository, AmenityRepository amenityRepository, HotelAmenityRepository hotelAmenityRepository) {
        this.hotelRepository = hotelRepository;
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.contactsRepository = contactsRepository;
        this.arrivalTimeRepository = arrivalTimeRepository;
        this.amenityRepository = amenityRepository;
        this.hotelAmenityRepository = hotelAmenityRepository;
    }

    @Transactional
    public Hotel saveHotel(CreateHotelRequest request) {
        Address address = saveAddress(request.getAddress());
        ArrivalTime arrivalTime = saveArrivalTime(request.getArrivalTime());
        Hotel hotel = Hotel.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .description(request.getDescription())
                .address(address)
                .arrivalTime(arrivalTime)
                .build();

        hotel = hotelRepository.save(hotel);
        Contact contact = saveContact(request.getContact(), hotel);
        hotel.setContact(contact);
        return hotel;
    }

    private Address saveAddress(AddressDto addressDto) {
        Country country = countryRepository.findByNameIgnoreCase(addressDto.getCountry())
                .orElseGet(() -> countryRepository.save(new Country(null, addressDto.getCountry())));
        City city = cityRepository.findByNameIgnoreCaseAndCountry(addressDto.getCity(), country)
                .orElseGet(() -> cityRepository.save(new City(null, addressDto.getCity(), country)));

        return addressRepository.save(new Address(null, addressDto.getHouseNumber(), addressDto.getStreet(), addressDto.getPostCode(), city));
    }

    private Contact saveContact(ContactDto contactDto, Hotel hotel) {
        return contactsRepository.save(new Contact(null, contactDto.getPhone(), contactDto.getEmail(), hotel));
    }

    private ArrivalTime saveArrivalTime(ArrivalTimeDto arrivalTimeDto) {
        LocalTime checkIn = arrivalTimeDto.getCheckIn() != null ? LocalTime.parse(arrivalTimeDto.getCheckIn()) : null;
        LocalTime checkOut = arrivalTimeDto.getCheckOut() != null ? LocalTime.parse(arrivalTimeDto.getCheckOut()) : null;
        if (checkIn == null && checkOut == null) return null;

        return arrivalTimeRepository.findByCheckInAndCheckOut(checkIn, checkOut)
                .orElseGet(() -> arrivalTimeRepository.save(new ArrivalTime(null, checkIn, checkOut)));

    }

    public void saveAmenities(Hotel hotel, List<String> amenities){
        List<Amenity> existingAmenities = amenityRepository.findByNameIgnoreCaseIn(amenities);

        Set<String> existingNames = existingAmenities.stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet());

        List<Amenity> newAmenities = amenities.stream()
                .filter(name -> !existingNames.contains(name))
                .map(Amenity::new)
                .collect(Collectors.toList());

        if (!newAmenities.isEmpty()) {
            newAmenities = amenityRepository.saveAll(newAmenities);
            existingAmenities.addAll(newAmenities);
        }

        List<HotelAmenity> hotelAmenities = existingAmenities.stream()
                .map(amenity -> new HotelAmenity(new HotelAmenityId(hotel.getId(), amenity.getId()), hotel, amenity))
                .collect(Collectors.toList());

        hotelAmenityRepository.saveAll(hotelAmenities);
    }
}
