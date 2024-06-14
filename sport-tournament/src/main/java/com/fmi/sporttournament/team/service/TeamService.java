package com.fmi.sporttournament.team.service;

import com.fmi.sporttournament.team.dto.request.TeamRegistrationRequest;

import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.participant.service.ParticipantService;

import com.fmi.sporttournament.user.entity.User;

import com.fmi.sporttournament.team.mapper.TeamMapper;

import com.fmi.sporttournament.team.repository.TeamRepository;

import com.fmi.sporttournament.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final ParticipantService participantService;
    private final UserService userService;
    private final TeamMapper teamMapper;


    private Team validateTeamIdExist(Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()) {
            throw new IllegalArgumentException("Team with this id " + id + " doesn't exist");
        }
        return team.get();
    }

    private Team validateTeamNameExist(String teamName) {
        Optional<Team> team = teamRepository.findByName(teamName);
        if (team.isEmpty()) {
            throw new IllegalArgumentException("Team with this name " + teamName + " doesn't exist");
        }
        return team.get();
    }

    public Team createTeam(TeamRegistrationRequest teamRegistrationRequest) {
        String teamName = teamRegistrationRequest.getName();

        if (teamRepository.findByName(teamName).isPresent()) {
            throw new IllegalArgumentException("Team with this name already exists");
        }

        Team team = teamMapper.dtoToTeam(teamRegistrationRequest);
        User currentUser = userService.getCurrentUser();
        team = teamRepository.save(team);
        participantService.create(currentUser, team);
        return team;
    }

    public void deleteTeamById(Long id) {
        validateTeamIdExist(id);
        teamRepository.deleteById(id);
    }

    public void deleteTeamByTournamentName(String teamName) {
        Team team = validateTeamNameExist(teamName);
        teamRepository.deleteById(team.getId());
    }
}
