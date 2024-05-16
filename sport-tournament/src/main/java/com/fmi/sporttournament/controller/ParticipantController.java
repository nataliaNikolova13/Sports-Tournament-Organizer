package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.requests.ParticipantRequest;
import com.fmi.sporttournament.Dto.responses.ParticipantResponse;
import com.fmi.sporttournament.entity.Participant;
import com.fmi.sporttournament.entity.User;
import com.fmi.sporttournament.mapper.ParticipantMapper;
import com.fmi.sporttournament.services.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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
        } catch (IllegalStateException e) {
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
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
