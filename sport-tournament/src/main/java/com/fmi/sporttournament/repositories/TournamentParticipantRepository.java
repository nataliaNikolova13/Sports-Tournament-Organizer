package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.TournamentParticipant;

import com.fmi.sporttournament.entity.enums.TournamentParticipantStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentParticipantRepository extends JpaRepository<TournamentParticipant, Long> {
    Optional<TournamentParticipant> findByTournamentAndTeam(Tournament tournament, Team team);

    boolean existsByTournamentIdAndTeamIdAndStatus(Long tournamentId, Long teamId, TournamentParticipantStatus status);

    boolean existsByTournamentAndTeam(Tournament tournament, Team team);

    List<Team> findByTournament(Tournament tournament);
}
