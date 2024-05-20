package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.Match;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository  extends JpaRepository<Match, Long> {
    Optional<Match> findById(Long Id);
    List<Match> findByTournamentId(Long tournamentId);
    List<Match> findAll();
}
