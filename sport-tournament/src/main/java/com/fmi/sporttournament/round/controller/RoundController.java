package com.fmi.sporttournament.round.controller;

import com.fmi.sporttournament.round.dto.response.RoundResponse;
import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.round.mapper.RoundMapper;
import com.fmi.sporttournament.round.service.RoundService;
import com.fmi.sporttournament.tournament.dto.response.TournamentResponse;
import com.fmi.sporttournament.tournament.entity.Tournament;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/round")
public class RoundController {
    private final RoundService roundService;
    private final RoundMapper roundMapper;

    @GetMapping("/{id}")
    public ResponseEntity<RoundResponse> getRoundById(@PathVariable Long id) {
        Optional<Round> round = roundService.getRoundById(id);
        return round.map(
                value -> new ResponseEntity<>(roundMapper.roundToResponse(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
