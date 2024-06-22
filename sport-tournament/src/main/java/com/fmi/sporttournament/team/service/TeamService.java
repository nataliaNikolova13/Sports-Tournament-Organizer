package com.fmi.sporttournament.team.service;

import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;
import com.fmi.sporttournament.participant.repository.ParticipantRepository;
import com.fmi.sporttournament.team.dto.request.TeamRequest;
import com.fmi.sporttournament.exception.business.OperationNotAllowedException;

import com.fmi.sporttournament.participant.repository.ParticipantRepository;
import com.fmi.sporttournament.participant.service.ParticipantValidationService;
import com.fmi.sporttournament.participant.service.ParticipantService;

import com.fmi.sporttournament.team.dto.request.TeamRequest;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.entity.category.TeamCategory;
import com.fmi.sporttournament.team.mapper.TeamMapper;
import com.fmi.sporttournament.team.repository.TeamRepository;

import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;
import com.fmi.sporttournament.tournament_participant.repository.TournamentParticipantRepository;

import com.fmi.sporttournament.user.entity.User;
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
    private final TeamMapper teamMapper;
    private final TeamValidationService teamValidationService;

    private final ParticipantService participantService;
    private final ParticipantValidationService participantValidationService;

    private final ParticipantRepository participantRepository;

    private final TournamentParticipantRepository tournamentParticipantRepository;

    private final UserService userService;

    private void validateTeamNotParticipateInTournament(Team team) {
        if (!tournamentParticipantRepository.findTournamentsByTeamAndStatus(team, TournamentParticipantStatus.joined)
            .isEmpty()) {
            throw new OperationNotAllowedException(
                "The team " + team.getName() + " is participating in a tournament and it can't be deleted");
        }
    }

    public Team getTeamById(Long id) {
        return teamValidationService.validateTeamIdExist(id);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Team> getAllActiveTeams() {
        return participantRepository.findTeamsWithJoinedParticipants();
    }

    public Team createTeam(TeamRequest teamRegistrationRequest) {
        String teamName = teamRegistrationRequest.getName();

        teamValidationService.validateTeamNameNotExist(teamName);

        Team team = teamMapper.requestToTeam(teamRegistrationRequest);
        User currentUser = userService.getCurrentUser();
        teamValidationService.validateUserAgeAndTeamCategory(currentUser, team);
        team = teamRepository.save(team);
        participantService.create(currentUser, team);
        return team;
    }

    public void deleteTeamById(Long id) {
        Team team = teamValidationService.validateTeamIdExist(id);
        validateTeamNotParticipateInTournament(team);
        teamRepository.deleteById(id);
    }

    public void deleteTeamByTournamentName(String teamName) {
        Team team = teamValidationService.validateTeamNameExist(teamName);
        validateTeamNotParticipateInTournament(team);
        teamRepository.deleteById(team.getId());
    }

    public Team updateTeam(Long id, TeamRequest teamRequest) {
        Team team = teamValidationService.validateTeamIdExist(id);
        String teamName = teamRequest.getName();
        TeamCategory teamCategory = teamRequest.getCategory();
        updateTeamName(team, teamName);
        return updateCategory(team, teamCategory);
    }

    private Team updateTeamName(Team team, String newTeamName) {
        if (!team.getName().equals(newTeamName)) {
            teamValidationService.validateTeamNameNotExist(newTeamName);
        }
        validateTeamNotParticipateInTournament(team);

        team.setName(newTeamName);
        return teamRepository.save(team);
    }

    public Team updateTeamNameById(Long id, String newLocationName) {
        Team team = teamValidationService.validateTeamIdExist(id);
        return updateTeamName(team, newLocationName);
    }

    public Team updateTeamNameByTeamName(String teamName, String newLocationName) {
        Team team = teamValidationService.validateTeamNameExist(teamName);
        return updateTeamName(team, newLocationName);
    }

    private Team updateCategory(Team team, TeamCategory category) {
        validateTeamNotParticipateInTournament(team);
        if (category == TeamCategory.youth) {
            participantValidationService.validateAgeAllParticipantsInTeam(team);
        }
        team.setCategory(category);
        return teamRepository.save(team);
    }

    public Team updateCategoryById(Long id, TeamCategory teamCategory) {
        Team team = teamValidationService.validateTeamIdExist(id);
        return updateCategory(team, teamCategory);
    }

    public Team updateCategoryByTeamName(String teamName, TeamCategory teamCategory) {
        Team team = teamValidationService.validateTeamNameExist(teamName);
        return updateCategory(team, teamCategory);
    }

    public List<Team> getTeamsForUser(User user) {
        List<Participant> participants = participantRepository.findByUser(user);
        return participants.stream()
                .filter(participant -> participant.getStatus().equals(ParticipantStatus.joined))
                .map(Participant::getTeam)
                .collect(Collectors.toList());
    }

//    public Optional<Team> getTeamById(Long teamId) {
//        return teamRepository.findById(teamId);
//    }
}
