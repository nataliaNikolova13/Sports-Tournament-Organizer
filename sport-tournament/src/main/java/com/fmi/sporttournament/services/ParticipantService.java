package com.fmi.sporttournament.services;

import com.fmi.sporttournament.entity.Participant;
import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.entity.User;
import com.fmi.sporttournament.entity.enums.ParticipantStatus;
import com.fmi.sporttournament.repositories.ParticipantRepository;
import com.fmi.sporttournament.repositories.UserRepositoty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserRepositoty userRepositoty;

    public Participant create(User user, Team team){
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setTeam(team);
        participant.setStatus(ParticipantStatus.joined);
        return participantRepository.save(participant);
    }

//    public void addParticipantToTeam(Long userId, Long teamId) {
//        Optional<User> user = userRepositoty.findById(userId);
//        //to do
//    }
}