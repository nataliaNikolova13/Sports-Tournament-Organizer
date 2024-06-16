package com.fmi.sporttournament.match_result.controller;

import com.fmi.sporttournament.match_result.dto.request.MatchResultTeamTournamentRequest;
import com.fmi.sporttournament.match_result.dto.response.MatchResultResponse;
import com.fmi.sporttournament.match_result.entity.MatchResult;
import com.fmi.sporttournament.match_result.mapper.MatchResultMapper;

import com.fmi.sporttournament.match_result.service.MatchResultService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match-result")
public class MatchResultController {
    private final MatchResultService matchResultService;
    private final MatchResultMapper matchResultMapper;
    @GetMapping("/{id}")
    public ResponseEntity<MatchResultResponse> getMatchResultById(@PathVariable Long id) {
        Optional<MatchResult> matchResult =  matchResultService.getMatchResultById(id);
        return matchResult.map(
                value -> new ResponseEntity<>(matchResultMapper.matchResultToResponse(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tournament/name/{tournamentName}")
    public ResponseEntity<List<MatchResultResponse>> getMatchByTournamentName(@PathVariable String tournamentName) {
        try {
            List<MatchResult> matches = matchResultService.getMatchResultsForTournamentByTournamentName(tournamentName);
            return ResponseEntity.ok(matchResultMapper.matchResultsToResponse(matches));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tournament/team")
    public ResponseEntity<List<MatchResultResponse>> getMatchByTournamentAndTeam(@RequestBody
                                                                           MatchResultTeamTournamentRequest matchResultTeamTournamentRequest) {
        try {
            String tournamentName = matchResultTeamTournamentRequest.getTournamentName();
            String teamName = matchResultTeamTournamentRequest.getTeamName();
            List<MatchResult> matches = matchResultService.getMatchResultsForTournamentByTournamentAndTeam(tournamentName, teamName);
            return ResponseEntity.ok(matchResultMapper.matchResultsToResponse(matches));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}