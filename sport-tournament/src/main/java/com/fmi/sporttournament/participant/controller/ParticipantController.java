package com.fmi.sporttournament.participant.controller;

import com.fmi.sporttournament.participant.dto.request.ParticipantRequest;
import com.fmi.sporttournament.participant.dto.response.ParticipantResponse;
import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.participant.mapper.ParticipantMapper;
import com.fmi.sporttournament.participant.service.ParticipantService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participant")
public class ParticipantController {
    private final ParticipantService participantService;
    private final ParticipantMapper participantMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<ParticipantResponse> getMatchById(@PathVariable Long id) {
        Participant participant = participantService.getParticipantById(id);
        return ResponseEntity.ok(participantMapper.participantToResponse(participant));
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<ParticipantResponse> addParticipant(@RequestBody @Valid ParticipantRequest participantRequest) {
        Participant participant = participantService.addParticipantToTeam(participantRequest);
        return ResponseEntity.ok(participantMapper.participantToResponse(participant));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<ParticipantResponse> removeParticipant(@RequestBody @Valid ParticipantRequest participantRequest) {
        Participant participant = participantService.removeParticipantFromTeam(participantRequest);
        return ResponseEntity.ok(participantMapper.participantToResponse(participant));
    }

    @GetMapping("/team/{teamId}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<List<Participant>> getParticipantsByTeamId(@PathVariable Long teamId) {
        List<Participant> participants = participantService.getParticipantsByTeamId(teamId);
        return ResponseEntity.ok(participants);
    }
}
