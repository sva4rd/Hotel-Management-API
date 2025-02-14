package com.project.hotels.repository;

import com.project.hotels.model.Hotel;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    @Query("""
        SELECT h FROM Hotel h
        LEFT JOIN FETCH h.address a
        LEFT JOIN FETCH a.city c
        LEFT JOIN FETCH c.country
        LEFT JOIN FETCH h.contact
    """)
    List<Hotel> findAllWithShortDetails();

    @Query("""
        SELECT h FROM Hotel h
        LEFT JOIN FETCH h.address a
        LEFT JOIN FETCH a.city c
        LEFT JOIN FETCH c.country
        LEFT JOIN FETCH h.contact
        LEFT JOIN FETCH h.arrivalTime
        LEFT JOIN FETCH h.hotelAmenities ha
        LEFT JOIN FETCH ha.amenity am
        WHERE h.id = :id
    """)
    Optional<Hotel> findByIdWithFullDetails(Long id);

    @EntityGraph(attributePaths = {"address.city.country", "contact"})
    @Nonnull
    List<Hotel> findAll(@Nonnull Specification<Hotel> spec);
}
