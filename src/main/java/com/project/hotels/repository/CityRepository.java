package com.project.hotels.repository;

import com.project.hotels.model.City;
import com.project.hotels.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameIgnoreCaseAndCountry(String name, Country country);
}
