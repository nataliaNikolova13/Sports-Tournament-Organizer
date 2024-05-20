package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.TeamResult;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamResultRepository extends JpaRepository<TeamResult, Long> {
    Optional<TeamResult> findById(Long Id);
    Optional<TeamResult> findByTeamId(Long teamId);
    List<TeamResult> findByTournamentId(Long tournamentId);
    List<TeamResult> findAll();
}
