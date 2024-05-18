package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.Tournament;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Optional<Tournament> findByTournamentName(String tournamentName);
    Optional<Tournament> findById(Long id);
    List<Tournament> findBySportType(String sportType);
    List<Tournament> findByLocationLocationName(String locationName);
    List<Tournament> findAll();

}
