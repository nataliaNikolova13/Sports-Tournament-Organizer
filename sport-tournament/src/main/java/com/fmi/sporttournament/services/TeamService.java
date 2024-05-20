package com.fmi.sporttournament.services;

import com.fmi.sporttournament.Dto.requests.TeamRegistrationRequest;

import com.fmi.sporttournament.entity.Participant;
import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.entity.User;

import com.fmi.sporttournament.mapper.TeamMapper;

import com.fmi.sporttournament.repositories.TeamRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final ParticipantService participantService;
    private final UserService userService;
    private final TeamMapper teamMapper;

    public Team createTeam(TeamRegistrationRequest teamRegistrationRequest) {
        String teamName = teamRegistrationRequest.getName();

        if (teamRepository.findByName(teamName).isPresent()) {
            throw new IllegalArgumentException("Team with this name already exists");
        }

        Team team = teamMapper.dtoToTeam(teamRegistrationRequest);
        User currentUser = userService.getCurrentUser();
        Participant participant = participantService.create(currentUser, team);
        return teamRepository.save(team);
    }

//    public Optional<Team> getById(Long id){
//        return teamRepository.findById(id);
//    }
}
