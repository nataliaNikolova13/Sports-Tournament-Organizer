package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.Location;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findById(Long id);

    Optional<Location> findByLocationName(String tournamentName);

    @Query("SELECT COUNT(v) FROM Venue v WHERE v.location.id = :locationId")
    Long countVenuesByLocationId(Long locationId);

    @Query("SELECT COUNT(v) FROM Venue v WHERE v.location.locationName = :locationName")
    Long countVenuesByLocationName(String locationName);

    List<Location> findAll();
}
