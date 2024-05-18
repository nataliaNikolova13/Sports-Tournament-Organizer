package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.Location;

import com.fmi.sporttournament.entity.Tournament;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    Optional<Location> findByLocationName(String tournamentName);
    boolean existsByLocationName(String tournamentName);
    List<Location> findAll();
}
