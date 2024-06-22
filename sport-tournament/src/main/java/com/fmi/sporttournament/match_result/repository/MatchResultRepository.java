package com.fmi.sporttournament.match_result.repository;

import com.fmi.sporttournament.match_result.entity.MatchResult;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.tournament.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
    Optional<MatchResult> findById(Long id);

    MatchResult findByMatchId(Long matchId);

    @Query("SELECT mr FROM MatchResult mr WHERE mr.match.round.tournament = :tournament")
    List<MatchResult> findAllByTournament(Tournament tournament);

    @Query("SELECT mr FROM MatchResult mr WHERE mr.match.round.tournament = :tournament AND (mr.match.team1 = :team OR mr.match.team2 = :team)")
    List<MatchResult> findAllByTournamentAndTeam(Tournament tournament, Team team);
}
