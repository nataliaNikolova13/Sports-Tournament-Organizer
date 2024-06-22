package com.fmi.sporttournament.match_result.service;

import com.fmi.sporttournament.match.entity.Match;

import com.fmi.sporttournament.match_result.dto.request.MatchResultRequest;
import com.fmi.sporttournament.match_result.entity.MatchResult;
import com.fmi.sporttournament.match_result.mapper.MatchResultMapper;

import com.fmi.sporttournament.match_result.repository.MatchResultRepository;
import com.fmi.sporttournament.team.entity.Team;

import com.fmi.sporttournament.team.repository.TeamRepository;
import com.fmi.sporttournament.team.service.TeamService;
import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.tournament.repository.TournamentRepository;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Data
@RequiredArgsConstructor
public class MatchResultService {
    private final MatchResultMapper matchResultMapper;
    private final MatchResultRepository matchResultRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final TeamService teamService;
    private final UserService userService;

    private Tournament validTournamentExist(String tournamentName) {
        Optional<Tournament> tournament = tournamentRepository.findByTournamentName(tournamentName);
        if (tournament.isEmpty()) {
            throw new IllegalArgumentException("The tournament doesn't exist");
        }
        return tournament.get();
    }

    private Team validTeamExist(String teamName) {
        Optional<Team> team = teamRepository.findByName(teamName);
        if (team.isEmpty()) {
            throw new IllegalArgumentException("The team doesn't exist");
        }
        return team.get();
    }


    public Optional<MatchResult> getMatchResultById(Long id){
        return matchResultRepository.findById(id);
    }

    public List<MatchResult> getMatchResultsForTournamentByTournamentName(String tournamentName) {
        Tournament tournament = validTournamentExist(tournamentName);
        return matchResultRepository.findAllByTournament(tournament);
    }

    public List<MatchResult> getMatchResultsForTournamentByTournamentAndTeam(String tournamentName, String teamName) {
        Tournament tournament = validTournamentExist(tournamentName);
        Team team = validTeamExist(teamName);
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

        MatchResultRequest matchResultRequest = new MatchResultRequest(match, winnerTeam, firstTeamScore, secondTeamScore);

        MatchResult matchResult = matchResultMapper.requestToMatchResult(matchResultRequest);
        return matchResultRepository.save(matchResult);
    }

    public List<MatchResult> getAllMatchResultsForUser() {
        User user = userService.getCurrentUser();
        List<Team> userTeams = teamService.getTeamsForUser(user);
        List<MatchResult> allMatchResults = new ArrayList<>();

        for (Team team : userTeams) {
            List<MatchResult> teamMatchResults = matchResultRepository.findByMatch_Team1OrMatch_Team2(team, team);
            allMatchResults.addAll(teamMatchResults);
        }

        return allMatchResults;
    }
}
