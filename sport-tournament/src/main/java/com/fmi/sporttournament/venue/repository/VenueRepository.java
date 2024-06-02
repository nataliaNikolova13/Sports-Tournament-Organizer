package com.fmi.sporttournament.venue.repository;

import com.fmi.sporttournament.venue.entity.Venue;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> findById(Long id);

    List<Venue> findByLocationLocationName(String locationName);

    @Modifying
    @Transactional
    void deleteByLocationLocationNameAndNumber(String locationName, Long number);
}
