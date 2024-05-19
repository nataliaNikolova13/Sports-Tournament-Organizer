package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.TeamResultDto;
import com.fmi.sporttournament.entity.TeamResult;
import com.fmi.sporttournament.mapper.TeamResultMapper;
import com.fmi.sporttournament.services.TeamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team-result")
public class TeamResultController {

    private final TeamResultService teamResultService;
    private final TeamResultMapper teamResultMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TeamResultDto> getTeamResultById(@PathVariable Long id) {
        Optional<TeamResult> teamResultOptional = teamResultService.getMatchById(id);
        return teamResultOptional.map(teamResult -> new ResponseEntity<>(teamResultMapper.teamResultToDto(teamResult), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<TeamResultDto> getTeamResultByTeamId(@PathVariable Long teamId) {
        Optional<TeamResult> teamResultOptional = teamResultService.getMatchByTeamId(teamId);
        return teamResultOptional.map(teamResult -> new ResponseEntity<>(teamResultMapper.teamResultToDto(teamResult), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<TeamResultDto>> getTeamResultByTournamentId(@PathVariable Long tournamentId) {
        List<TeamResult> teamResults = teamResultService.getTeamResultByTournamentId(tournamentId);
        return new ResponseEntity<>(teamResultMapper.teamResultsToTeamResultDtos(teamResults), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TeamResultDto>> getAllTeamResults() {
        List<TeamResult> allTeamResults = teamResultService.getAllMatches();
        return new ResponseEntity<>(teamResultMapper.teamResultsToTeamResultDtos(allTeamResults), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTeamResult(@PathVariable Long id) {
        teamResultService.removeTeamResult(id);
        return ResponseEntity.ok().build();
    }
}
