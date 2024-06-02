package com.fmi.sporttournament.round.repository;

import com.fmi.sporttournament.round.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoundRepository  extends JpaRepository<Round, Long> {
    Optional<Round> findByTournamentId(Long id);
}
