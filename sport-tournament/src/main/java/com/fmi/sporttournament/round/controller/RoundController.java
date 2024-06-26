package com.fmi.sporttournament.round.controller;

import com.fmi.sporttournament.round.dto.response.MatchDetailsDto;
import com.fmi.sporttournament.round.dto.response.RoundResponse;
import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.round.mapper.RoundMapper;
import com.fmi.sporttournament.round.service.RoundService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/round")
public class RoundController {
    private final RoundService roundService;
    private final RoundMapper roundMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<RoundResponse> getRoundById(@PathVariable Long id) {
        Round round = roundService.getRoundById(id);
        return ResponseEntity.ok(roundMapper.roundToResponse(round));
    }

    @GetMapping("/tournament/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<List<Round>> getRoundByTournamentId(@PathVariable Long id) {
        List<Round> rounds = roundService.getRoundByTournamentId(id);
        return new ResponseEntity<>(rounds, HttpStatus.OK);
    }

    @GetMapping("/{roundId}/matches")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public List<MatchDetailsDto> getMatchesByRoundId(@PathVariable Long roundId) {
        return roundService.getMatchesByRoundId(roundId);
    }
}
