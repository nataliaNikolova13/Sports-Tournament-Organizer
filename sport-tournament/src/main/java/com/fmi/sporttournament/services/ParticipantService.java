package com.fmi.sporttournament.services;

import com.fmi.sporttournament.Dto.requests.ParticipantRequest;
import com.fmi.sporttournament.entity.Participant;
import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.entity.User;
import com.fmi.sporttournament.entity.enums.ParticipantStatus;
import com.fmi.sporttournament.repositories.ParticipantRepository;
import com.fmi.sporttournament.repositories.TeamRepository;
import com.fmi.sporttournament.repositories.UserRepositoty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserRepositoty userRepositoty;
    private final TeamRepository teamRepository;

    public Participant create(User user, Team team){
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setTeam(team);
        participant.setStatus(ParticipantStatus.joined);
        return participantRepository.save(participant);
    }

    public boolean isUserAlreadyInTeam(User user, Team team) {
        return participantRepository.existsByUserAndTeam(user, team);
    }

    public Participant addParticipantToTeam(ParticipantRequest participantRequest) {
        Optional<User> user = userRepositoty.findById(participantRequest.getUserId());
        if(!user.isPresent()){
            throw new IllegalArgumentException("User not found");
        }
        Optional<Team> team = teamRepository.findById(participantRequest.getTeamId());
        if(!team.isPresent()){
            throw new IllegalArgumentException("Team not found");
        }
        if(isUserAlreadyInTeam(user.get(), team.get())){
            throw new IllegalStateException("User is already a participant of the team");
        }
        return create(user.get(), team.get());
    }
}