package com.fmi.sporttournament.tournament_participant.repository;

import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.tournament_participant.entity.TournamentParticipant;

import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentParticipantRepository extends JpaRepository<TournamentParticipant, Long> {
    Optional<TournamentParticipant> findByTournamentAndTeam(Tournament tournament, Team team);

    boolean existsByTournamentIdAndTeamIdAndStatus(Long tournamentId, Long teamId, TournamentParticipantStatus status);

    boolean existsByTournamentAndTeam(Tournament tournament, Team team);

    List<TournamentParticipant> findByTournament(Tournament tournament);

    @Query("SELECT tp.team FROM TournamentParticipant tp WHERE tp.status = :status AND tp.tournament = :tournament ORDER BY tp.timeStamp ASC")
    List<Team> findAllTeamsByTournamentStatusAndTournament(TournamentParticipantStatus status, Tournament tournament);

    @Query("SELECT DISTINCT tp.tournament FROM TournamentParticipant tp WHERE tp.team = :team AND tp.status = :status")
    List<Tournament> findTournamentsByTeamAndStatus(Team team, TournamentParticipantStatus status);

    @Query("SELECT tp FROM TournamentParticipant tp WHERE tp.status = :status AND tp.tournament.endAt < :currentDate")
    List<TournamentParticipant> findAllParticipantsWithStatusJoinedAndTournamentEnded(TournamentParticipantStatus status, Date currentDate);
}
