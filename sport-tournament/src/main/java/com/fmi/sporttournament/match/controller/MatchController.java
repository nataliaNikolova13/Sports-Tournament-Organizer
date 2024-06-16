package com.fmi.sporttournament.match.controller;

import com.fmi.sporttournament.match.dto.request.MatchTeamTournamentRequest;
import com.fmi.sporttournament.match.dto.response.MatchResponse;
import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.match.mapper.MatchMapper;
import com.fmi.sporttournament.match.service.MatchService;
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
@RequestMapping("/match")
public class MatchController {
    private final MatchService matchService;
    private final MatchMapper matchMapper;
    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> getMatchById(@PathVariable Long id) {
        Optional<Match> match = matchService.getMatchById(id);
        return match.map(
                value -> new ResponseEntity<>(matchMapper.matchToResponse(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tournament/name/{tournamentName}")
    public ResponseEntity<List<MatchResponse>> getMatchByTournamentName(@PathVariable String tournamentName) {
        try {
            List<Match> matches = matchService.getMatchesByTournamentName(tournamentName);
            return ResponseEntity.ok(matchMapper.matchesToResponse(matches));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tournament/team")
    public ResponseEntity<List<MatchResponse>> getMatchByTournamentAndTeam(@RequestBody MatchTeamTournamentRequest matchTeamTournamentRequest) {
        try {
            String tournamentName = matchTeamTournamentRequest.getTournamentName();
            String teamName = matchTeamTournamentRequest.getTeamName();
            List<Match> matches = matchService.getMatchesByTournamentNameAndTeamName(tournamentName, teamName);
            return ResponseEntity.ok(matchMapper.matchesToResponse(matches));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
