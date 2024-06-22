package com.fmi.sporttournament.match.controller;

import com.fmi.sporttournament.match.dto.request.MatchTeamTournamentRequest;
import com.fmi.sporttournament.match.dto.response.MatchResponse;
import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.match.mapper.MatchMapper;
import com.fmi.sporttournament.match.service.MatchService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {
    private final MatchService matchService;
    private final MatchMapper matchMapper;

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> getMatchById(@PathVariable Long id) {
        Match match = matchService.getMatchById(id);
        return ResponseEntity.ok(matchMapper.matchToResponse(match));
    }

    @GetMapping("/tournament/name/{tournamentName}")
    public ResponseEntity<List<MatchResponse>> getMatchByTournamentName(@PathVariable String tournamentName) {
        List<Match> matches = matchService.getMatchesByTournamentName(tournamentName);
        return ResponseEntity.ok(matchMapper.matchesToResponse(matches));
    }

    @GetMapping("/tournament/team")
    public ResponseEntity<List<MatchResponse>> getMatchByTournamentAndTeam(
        @RequestBody @Valid MatchTeamTournamentRequest matchTeamTournamentRequest) {
        String tournamentName = matchTeamTournamentRequest.getTournamentName();
        String teamName = matchTeamTournamentRequest.getTeamName();
        List<Match> matches = matchService.getMatchesByTournamentNameAndTeamName(tournamentName, teamName);
        return ResponseEntity.ok(matchMapper.matchesToResponse(matches));
    }
}
