package com.fmi.sporttournament.tournament_participant.controller;

import com.fmi.sporttournament.tournament_participant.dto.request.TournamentParticipantRequest;
import com.fmi.sporttournament.tournament_participant.dto.response.TournamentParticipantResponse;
import com.fmi.sporttournament.tournament_participant.entity.TournamentParticipant;
import com.fmi.sporttournament.tournament_participant.mapper.TournamentParticipantMapper;
import com.fmi.sporttournament.tournament_participant.service.TournamentParticipantService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<TournamentParticipantResponse> getMatchById(@PathVariable Long id) {
        TournamentParticipant tournamentParticipant = tournamentParticipantService.getTournamentParticipantById(id);
        return ResponseEntity.ok(tournamentParticipantMapper.tournamentParticipantToResponse(tournamentParticipant));
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<TournamentParticipantResponse> addTeamToTournament(@RequestBody
                                                                             @Valid TournamentParticipantRequest tournamentParticipantRequest) {
        TournamentParticipant tournamentParticipant = tournamentParticipantService.addTeamToTournament(
            tournamentParticipantRequest);
        return ResponseEntity.ok(
            tournamentParticipantMapper.tournamentParticipantToResponse(tournamentParticipant));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<Void> deleteParticipantFromTeam(
        @RequestBody @Valid TournamentParticipantRequest tournamentParticipantRequest) {
        TournamentParticipant tournamentParticipant = tournamentParticipantService.deleteParticipantFromTeam(
            tournamentParticipantRequest);
        return ResponseEntity.ok().build();
    }
}
