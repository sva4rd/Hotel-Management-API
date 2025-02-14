package com.project.hotels.repository;

import com.project.hotels.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    List<Amenity> findByNameIgnoreCaseIn(List<String> names);
}

