package com.fmi.sporttournament.tournament_participant.repository;

import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.tournament_participant.entity.TournamentParticipant;

import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentParticipantRepository extends JpaRepository<TournamentParticipant, Long> {
    Optional<TournamentParticipant> findByTournamentAndTeam(Tournament tournament, Team team);

    boolean existsByTournamentIdAndTeamIdAndStatus(Long tournamentId, Long teamId, TournamentParticipantStatus status);

    boolean existsByTournamentAndTeam(Tournament tournament, Team team);

    List<Team> findByTournament(Tournament tournament);

    @Query("SELECT tp.team FROM TournamentParticipant tp WHERE tp.status = :status ORDER BY tp.timeStamp ASC")
    List<Team> findAllTeamsByTournamentStatus(TournamentParticipantStatus status);

    @Query("SELECT DISTINCT tp.tournament FROM TournamentParticipant tp WHERE tp.team = :team AND tp.status = :status")
    List<Tournament> findTournamentsByTeam(Team team, TournamentParticipantStatus status);
}
