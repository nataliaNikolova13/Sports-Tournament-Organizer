package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.MatchDto;
import com.fmi.sporttournament.entity.Match;
import com.fmi.sporttournament.mapper.MatchMapper;
import com.fmi.sporttournament.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;
    private final MatchMapper matchMapper;

    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> getMatchById(@PathVariable Long id) {
        Optional<Match> matchOptional = matchService.getMatchById(id);
        return matchOptional.map(match -> new ResponseEntity<>(matchMapper.matchToDto(match), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<MatchDto>> getMatchesByTournamentId(@PathVariable Long tournamentId) {
        List<Match> matches = matchService.getMatchesByTournamentId(tournamentId);
        return new ResponseEntity<>(matchMapper.matchesToMatchDtos(matches), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MatchDto>> getAllMatches() {
        List<Match> allMatches = matchService.getAllMatches();
        return new ResponseEntity<>(matchMapper.matchesToMatchDtos(allMatches), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeMatch(@PathVariable Long id) {
        matchService.removeMatch(id);
        return ResponseEntity.ok().build();
    }
}
