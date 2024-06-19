package com.fmi.sporttournament.participant.controller;

import com.fmi.sporttournament.participant.dto.request.ParticipantRequest;
import com.fmi.sporttournament.participant.dto.response.ParticipantResponse;
import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.participant.mapper.ParticipantMapper;
import com.fmi.sporttournament.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participant")
public class ParticipantController {
    private final ParticipantService participantService;
    private final ParticipantMapper participantMapper;

    @PostMapping
    public ResponseEntity<ParticipantResponse> addParticipant(@RequestBody ParticipantRequest participantRequest){
        try{
            Participant participant = participantService.addParticipantToTeam(participantRequest);
            return ResponseEntity.ok(participantMapper.participantToResponse(participant));
        } catch (IllegalArgumentException| IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping
    public ResponseEntity<ParticipantResponse> removeParticipant(@RequestBody ParticipantRequest participantRequest){
        try{
            Participant participant = participantService.removeParticipantFromTeam(participantRequest);
            return ResponseEntity.ok(participantMapper.participantToResponse(participant));
        }catch (IllegalArgumentException| IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<List<Participant>> getParticipantsByTeamId(@PathVariable Long teamId) {
        List<Participant> participants = participantService.getParticipantsByTeamId(teamId);
        return ResponseEntity.ok(participants);
    }
}
