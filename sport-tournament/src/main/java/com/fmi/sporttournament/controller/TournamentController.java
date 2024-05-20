package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.requests.tournament.TournamentRegistrationRequest;

import com.fmi.sporttournament.Dto.responses.tournament.TournamentResponse;

import com.fmi.sporttournament.entity.Tournament;

import com.fmi.sporttournament.mapper.TournamentMapper;

import com.fmi.sporttournament.services.TournamentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournament")
public class TournamentController {
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Optional<Tournament> tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament.get());
        /*
        return tournament.map(value -> new ResponseEntity<>(tournamentMapper.tournamentToResponse(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));*/
    }
    @GetMapping("/name/{tournamentName}")
    public ResponseEntity<TournamentResponse> getTournamentByTournamentName(@PathVariable String tournamentName) {
        Optional<Tournament> tournamentOptional = tournamentService.getTournamentByTournamentName(tournamentName);
        return tournamentOptional.map(tournament -> new ResponseEntity<>(tournamentMapper.tournamentToResponse(tournament), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTournament(@PathVariable Long id) {
        tournamentService.removeTournament(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-tournament")
    public ResponseEntity<TournamentResponse> createLocation(@RequestBody TournamentRegistrationRequest tournamentRequest) {
        try{
            Tournament tournament = tournamentService.createTournament(tournamentRequest);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(tournament));
        } catch (IllegalStateException|IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
