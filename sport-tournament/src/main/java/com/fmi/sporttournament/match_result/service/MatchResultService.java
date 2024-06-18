package com.fmi.sporttournament.match_result.service;

import com.fmi.sporttournament.match.entity.Match;

import com.fmi.sporttournament.match_result.dto.request.MatchResultRequest;
import com.fmi.sporttournament.match_result.entity.MatchResult;
import com.fmi.sporttournament.match_result.mapper.MatchResultMapper;
import com.fmi.sporttournament.match_result.repository.MatchResultRepository;

import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.service.TeamValidationService;

import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.tournament.service.TournamentValidationService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Data
@RequiredArgsConstructor
public class MatchResultService {
    private final MatchResultMapper matchResultMapper;
    private final MatchResultRepository matchResultRepository;
    private final MatchResultValidationService matchResultValidationService;

    private final TournamentValidationService tournamentValidationService;

    private final TeamValidationService teamValidationService;

    public MatchResult getMatchResultById(Long id) {
        return matchResultValidationService.validateMatchResultExistById(id);
    }

    public List<MatchResult> getMatchResultsForTournamentByTournamentName(String tournamentName) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        return matchResultRepository.findAllByTournament(tournament);
    }

    public List<MatchResult> getMatchResultsForTournamentByTournamentAndTeam(String tournamentName, String teamName) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        Team team = teamValidationService.validateTeamNameExist(teamName);
        return matchResultRepository.findAllByTournamentAndTeam(tournament, team);
    }

    public MatchResult createMatchResult(Match match) {
        Team team1 = match.getTeam1();
        Team team2 = match.getTeam2();
        Random random = new Random();
        Integer firstTeamScore = random.nextInt(11);

        Integer secondTeamScore = random.nextInt(11);
        while (secondTeamScore == firstTeamScore) {
            secondTeamScore = random.nextInt(11);
        }

        Team winnerTeam = firstTeamScore > secondTeamScore ? team1 : team2;

        MatchResultRequest matchResultRequest =
            new MatchResultRequest(match, winnerTeam, firstTeamScore, secondTeamScore);

        MatchResult matchResult = matchResultMapper.requestToMatchResult(matchResultRequest);
        return matchResultRepository.save(matchResult);
    }
}
