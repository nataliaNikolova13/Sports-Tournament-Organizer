package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.Location;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findById(Long id);
    Optional<Location> findByLocationName(String tournamentName);
    List<Location> findAll();
}
