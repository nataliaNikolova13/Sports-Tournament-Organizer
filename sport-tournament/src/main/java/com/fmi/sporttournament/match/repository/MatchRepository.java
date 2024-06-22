package com.fmi.sporttournament.match.repository;

import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.tournament.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findById(Long id);

    @Query("SELECT m FROM Match m WHERE m.round.tournament = :tournament ORDER BY m.round.roundNumber")
    List<Match> findAllByTournamentOrderByRoundNumber(Tournament tournament);

    @Query("SELECT m FROM Match m WHERE m.round.tournament = :tournament AND (m.team1 = :team OR m.team2 = :team) ORDER BY m.round.roundNumber")
    List<Match> findAllByTournamentAndTeamOrderByRoundNumber( Tournament tournament, Team team);

    List<Match> findByRoundId(Long roundId);
}
