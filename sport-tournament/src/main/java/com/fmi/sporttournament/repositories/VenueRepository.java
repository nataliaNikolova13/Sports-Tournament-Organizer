package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.Venue;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> findById(Long id);
    List<Venue> findByLocationLocationName(String locationName);

}
