package com.fmi.sporttournament.team.service;

import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;
import com.fmi.sporttournament.participant.repository.ParticipantRepository;
import com.fmi.sporttournament.team.dto.request.TeamRequest;

import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.participant.service.ParticipantService;

import com.fmi.sporttournament.team.entity.category.TeamCategory;
import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;
import com.fmi.sporttournament.tournament_participant.repository.TournamentParticipantRepository;
import com.fmi.sporttournament.user.entity.User;

import com.fmi.sporttournament.team.mapper.TeamMapper;

import com.fmi.sporttournament.team.repository.TeamRepository;

import com.fmi.sporttournament.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final ParticipantService participantService;
    private final ParticipantRepository participantRepository;
    private final TournamentParticipantRepository tournamentParticipantRepository;
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
            throw new IllegalStateException("Team with this name " + teamName + " doesn't exist");
        }
        return team.get();
    }

    private void validateTeamNameNotExist(String teamName) {
        if (teamRepository.findByName(teamName).isPresent()) {
            throw new IllegalStateException("Team with the name " + teamName + " already exists");
        }
    }

    private void validateTeamNameIsNotBlank(String teamName) {
        if (teamName.isBlank()) {
            throw new IllegalStateException("Team name can't be blank");
        }
    }

    private void validateTeamNotParticipateInTournament(Team team) {
        if (!tournamentParticipantRepository.findTournamentsByTeamAndStatus(team, TournamentParticipantStatus.joined)
            .isEmpty()) {
            throw new IllegalStateException(
                "The team " + team.getName() + " is participating in a tournament and it can't be deleted");
        }
    }

    private void validateUserAgeAndTeamCategory(User user, Team team) {
        if (user.getAge() > 18 && team.getCategory() == TeamCategory.youth) {
            throw new IllegalStateException(
                "The user " + user.getUsername() + " is over 18 year and he/she can't be added to the youth team " +
                    team.getCategory());
        }
    }

    private void validateAgeAllParticipantsInTeam(Team team) {
        List<User> users = participantRepository.findUsersByTeam(team);
        for (User user : users) {
            if (user.getAge() > 18) {
                throw new IllegalStateException(
                    "The user " + user.getUsername() + " will be over 18 until the end of the tournament.");
            }
        }
    }

    public Team createTeam(TeamRequest teamRegistrationRequest) {
        String teamName = teamRegistrationRequest.getName();
        validateTeamNameIsNotBlank(teamName);
        validateTeamNameNotExist(teamName);

        if (teamRepository.findByName(teamName).isPresent()) {
            throw new IllegalArgumentException("Team with this name already exists");
        }

        Team team = teamMapper.requestToTeam(teamRegistrationRequest);
        User currentUser = userService.getCurrentUser();
        validateUserAgeAndTeamCategory(currentUser, team);
        team = teamRepository.save(team);
        participantService.create(currentUser, team);
        return team;
    }

    public void deleteTeamById(Long id) {
        Team team = validateTeamIdExist(id);
        validateTeamNotParticipateInTournament(team);
        teamRepository.deleteById(id);
    }

    public void deleteTeamByTournamentName(String teamName) {
        Team team = validateTeamNameExist(teamName);
        validateTeamNotParticipateInTournament(team);
        teamRepository.deleteById(team.getId());
    }

    public Team updateTeam(Long id, TeamRequest teamRequest){
        Team team = validateTeamIdExist(id);
        String teamName = teamRequest.getName();
        TeamCategory teamCategory = teamRequest.getCategory();
        updateTeamName(team, teamName);
        return updateCategory(team, teamCategory);
    }

    private Team updateTeamName(Team team, String newTeamName) {
        validateTeamNameIsNotBlank(newTeamName);

        if (!team.getName().equals(newTeamName)) {
            validateTeamNameNotExist(newTeamName);
        }
        validateTeamNotParticipateInTournament(team);

        team.setName(newTeamName);
        return teamRepository.save(team);
    }

    public Team updateTeamNameById(Long id, String newLocationName) {
        Team team = validateTeamIdExist(id);
        return updateTeamName(team, newLocationName);
    }

    public Team updateTeamNameByTeamName(String teamName, String newLocationName) {
        Team team = validateTeamNameExist(teamName);
        return updateTeamName(team, newLocationName);
    }

    private Team updateCategory(Team team, TeamCategory category) {
        validateTeamNotParticipateInTournament(team);
        if (category == TeamCategory.youth) {
            validateAgeAllParticipantsInTeam(team);
        }
        team.setCategory(category);
        return teamRepository.save(team);
    }

    public Team updateCategoryById(Long id, TeamCategory teamCategory) {
        Team team = validateTeamIdExist(id);
        return updateCategory(team, teamCategory);
    }

    public Team updateCategoryByTeamName(String teamName, TeamCategory teamCategory) {
        Team team = validateTeamNameExist(teamName);
        return updateCategory(team, teamCategory);
    }

    public List<Team> getTeamsForUser(User user) {
        List<Participant> participants = participantRepository.findByUser(user);
        return participants.stream()
                .filter(participant -> participant.getStatus().equals(ParticipantStatus.joined))
                .map(Participant::getTeam)
                .collect(Collectors.toList());
    }

    public Optional<Team> getTeamById(Long teamId) {
        return teamRepository.findById(teamId);
    }
}
