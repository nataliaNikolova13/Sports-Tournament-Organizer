package com.fmi.sporttournament.team.service;

import com.fmi.sporttournament.team.dto.request.TeamRegistrationRequest;

import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.participant.service.ParticipantService;
import com.fmi.sporttournament.user.entity.User;

import com.fmi.sporttournament.team.mapper.TeamMapper;

import com.fmi.sporttournament.team.repository.TeamRepository;

import com.fmi.sporttournament.user.service.UserService;
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
