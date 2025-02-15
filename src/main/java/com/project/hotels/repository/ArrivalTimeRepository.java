package com.project.hotels.repository;

import com.project.hotels.model.ArrivalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Optional;

public interface ArrivalTimeRepository extends JpaRepository<ArrivalTime, Long> {
    @Query("SELECT a FROM ArrivalTime a WHERE a.checkIn = :checkIn AND a.checkOut = :checkOut")
    Optional<ArrivalTime> findByCheckInAndCheckOut(@Param("checkIn") LocalTime checkIn, @Param("checkOut") LocalTime checkOut);

}
