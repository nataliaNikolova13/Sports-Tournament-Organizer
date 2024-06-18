package com.fmi.sporttournament.participant.service;

import com.fmi.sporttournament.exception.business.OperationNotAllowedException;
import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;

import com.fmi.sporttournament.participant.dto.request.ParticipantRequest;
import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;
import com.fmi.sporttournament.participant.repository.ParticipantRepository;

import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.service.TeamValidationService;

import com.fmi.sporttournament.tournament_participant.service.TournamentParticipantValidationService;

import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.service.UserValidationService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ParticipantValidationService participantValidationService;

    private final UserValidationService userValidationService;

    private final TeamValidationService teamValidationService;

    private final TournamentParticipantValidationService tournamentParticipantValidationService;

    public Participant create(User user, Team team) {
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setTeam(team);
        participant.setStatus(ParticipantStatus.joined);
        return participantRepository.save(participant);
    }

    private Participant remove(User user, Team team) {
        return changeStatus(user, team, ParticipantStatus.left);
    }

    private Participant rejoinTeam(User user, Team team) {
        return changeStatus(user, team, ParticipantStatus.joined);
    }

    private Participant changeStatus(User user, Team team, ParticipantStatus status) {
        Participant existingParticipant = participantValidationService.validateUserInTeam(user, team);
        existingParticipant.setStatus(status);
        existingParticipant.setTimeStamp(new Date());
        return participantRepository.save(existingParticipant);
    }

    private boolean isUserAlreadyInTeam(User user, Team team) {
        return participantRepository.existsByUserIdAndTeamIdAndStatus(user.getId(), team.getId(),
            ParticipantStatus.joined);
    }

    private boolean userAlreadyLeft(User user, Team team) {
        return participantRepository.existsByUserIdAndTeamIdAndStatus(user.getId(), team.getId(),
            ParticipantStatus.left);
    }

    public Participant getParticipantById(Long id) {
        return participantValidationService.validateParticipantExistById(id);
    }

    public Participant addParticipantToTeam(ParticipantRequest participantRequest) {
        User user = userValidationService.validateUserIdExist(participantRequest.getUserId());
        Team team = teamValidationService.validateTeamIdExist(participantRequest.getTeamId());
        participantValidationService.checkIfNoUserLeftInTeam(team);

        if (isUserAlreadyInTeam(user, team)) {
            throw new OperationNotAllowedException(
                "User with username " + user.getUsername() + " is already a participant of the team");
        }

        tournamentParticipantValidationService.validateTeamNotParticipateInTournament(team);
        participantValidationService.validateUserAgeAndTeamCategory(user, team);

        if (userAlreadyLeft(user, team)) {
            return rejoinTeam(user, team);
        }

        return create(user, team);
    }

    public Participant removeParticipantFromTeam(ParticipantRequest participantRequest) {
        User user = userValidationService.validateUserIdExist(participantRequest.getUserId());
        Team team = teamValidationService.validateTeamIdExist(participantRequest.getTeamId());

        if (!participantRepository.existsByUserAndTeam(user, team)) {
            throw new ResourceNotFoundException(
                "The user with username " + user.getUsername() + " hasn't been a member of the team " + team.getName());
        }
        if (userAlreadyLeft(user, team)) {
            throw new OperationNotAllowedException(
                "User with username " + user.getUsername() + " has already left the team " + team.getName());
        }
        tournamentParticipantValidationService.validateTeamNotParticipateInTournament(team);
        return remove(user, team);
    }
}