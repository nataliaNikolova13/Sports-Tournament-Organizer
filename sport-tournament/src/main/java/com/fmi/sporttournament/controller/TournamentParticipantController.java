package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.requests.tournament.TournamentParticipantRequest;
import com.fmi.sporttournament.Dto.responses.tournament.TournamentParticipantResponse;

import com.fmi.sporttournament.entity.TournamentParticipant;

import com.fmi.sporttournament.mapper.TournamentParticipantMapper;

import com.fmi.sporttournament.services.TournamentParticipantService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournament-participant")
public class TournamentParticipantController {
    private final TournamentParticipantService tournamentParticipantService;
    private final TournamentParticipantMapper tournamentParticipantMapper;

    @PostMapping("/add-team")
    public ResponseEntity<TournamentParticipantResponse> addTeamToTournament(@RequestBody
                                                                             TournamentParticipantRequest tournamentParticipantRequest) {
        try {
            TournamentParticipant tournamentParticipant = tournamentParticipantService.addTeamToTournament(
                tournamentParticipantRequest);
            return ResponseEntity.ok(
                tournamentParticipantMapper.tournamentParticipantToResponse(tournamentParticipant));
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/remove-team")
    public ResponseEntity<TournamentParticipantResponse> removeParticipantFromTeam(@RequestBody
                                                                                   TournamentParticipantRequest tournamentParticipantRequest) {
        try {
            TournamentParticipant tournamentParticipant = tournamentParticipantService.removeParticipantFromTeam(
                tournamentParticipantRequest);
            return ResponseEntity.ok(
                tournamentParticipantMapper.tournamentParticipantToResponse(tournamentParticipant));
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
