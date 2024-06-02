package com.fmi.sporttournament.participant.service;

import com.fmi.sporttournament.participant.dto.request.ParticipantRequest;

import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;

import com.fmi.sporttournament.participant.repository.ParticipantRepository;
import com.fmi.sporttournament.team.repository.TeamRepository;
import com.fmi.sporttournament.user.repository.UserRepositoty;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;
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

    public Participant remove(User user, Team team){
        Participant existintParticipant =
            participantRepository.findByUser(user).get(participantRepository.findByUser(user).size() - 1);
        existintParticipant.setStatus(ParticipantStatus.left);
        existintParticipant.setTimeStamp(new Date());
        return participantRepository.save(existintParticipant);
    }

    public boolean isUserAlreadyInTeam(User user, Team team) {
        return participantRepository.existsByUserIdAndTeamIdAndStatus(user.getId(), team.getId(), ParticipantStatus.joined);
    }

    public boolean userAlreadyLeft(User user, Team team){
        return participantRepository.existsByUserIdAndTeamIdAndStatus(user.getId(), team.getId(), ParticipantStatus.left);
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
        if(userAlreadyLeft(user.get(), team.get())){
            return rejoinTeam(user.get(), team.get());
        }
        return create(user.get(), team.get());
    }

    public Participant removeParticipantFromTeam(ParticipantRequest participantRequest){
        Optional<User> user = userRepositoty.findById(participantRequest.getUserId());
        if(!user.isPresent()){
            throw new IllegalArgumentException("User not found");
        }
        Optional<Team> team = teamRepository.findById(participantRequest.getTeamId());
        if(!team.isPresent()){
            throw new IllegalArgumentException("Team not found");
        }
        if(!participantRepository.existsByUserAndTeam(user.get(), team.get())){
            throw new IllegalStateException("User is not a member of the team");
        }
        if(userAlreadyLeft(user.get(), team.get())){
            throw new IllegalStateException("User has already left the team");
        }

        return remove(user.get(), team.get());
    }

    public Participant rejoinTeam(User user, Team team){
        Participant existintParticipant = participantRepository.findByUser(user).get(participantRepository.findByUser(user).size() - 1);
        existintParticipant.setStatus(ParticipantStatus.joined);
        existintParticipant.setTimeStamp(new Date());
        return participantRepository.save(existintParticipant);
    }
}