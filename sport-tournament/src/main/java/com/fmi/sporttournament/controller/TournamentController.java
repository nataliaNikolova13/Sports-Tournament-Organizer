package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.TournamentDto;

import com.fmi.sporttournament.entity.Tournament;

import com.fmi.sporttournament.mapper.TournamentMapper;

import com.fmi.sporttournament.services.TournamentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournament")
public class TournamentController {
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;

    @GetMapping
    public ResponseEntity<List<TournamentDto>> getAllTournaments() {
        List<Tournament> allTournament = tournamentService.getAllTournaments();
        return new ResponseEntity<>(tournamentMapper.tournamentsToTournamentDtos(allTournament), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentDto> getTournamentById(@PathVariable Long id) {
        Optional<Tournament> tournament = tournamentService.getTournamentById(id);
        return tournament.map(value -> new ResponseEntity<>(tournamentMapper.tournamentToDto(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/name/{tournamentName}")
    public ResponseEntity<TournamentDto> getTournamentByTournamentName(@PathVariable String tournamentName) {
        Optional<Tournament> tournamentOptional = tournamentService.getTournamentByTournamentName(tournamentName);
        return tournamentOptional.map(tournament -> new ResponseEntity<>(tournamentMapper.tournamentToDto(tournament), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/sport-type/{sportType}")
    public ResponseEntity<List<TournamentDto>> getTournamentBySportType(@PathVariable String sportType) {
        List<Tournament> tournaments = tournamentService.getTournamentBySportType(sportType);
        return new ResponseEntity<>(tournamentMapper.tournamentsToTournamentDtos(tournaments), HttpStatus.OK);
    }
    @GetMapping("/location/{locationName}")
    public ResponseEntity<List<TournamentDto>> getTournamentByLocation(@PathVariable String locationName) {
        List<Tournament> tournaments = tournamentService.getTournamentByLocationName(locationName);
        return new ResponseEntity<>(tournamentMapper.tournamentsToTournamentDtos(tournaments), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTournament(@PathVariable Long id) {
        tournamentService.removeTournament(id);
        return ResponseEntity.ok().build();
    }
}
