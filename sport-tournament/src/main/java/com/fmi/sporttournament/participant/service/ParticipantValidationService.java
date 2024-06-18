package com.fmi.sporttournament.participant.service;

import com.fmi.sporttournament.exception.business.BusinessRuleViolationException;
import com.fmi.sporttournament.exception.business.OperationNotAllowedException;
import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;

import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;
import com.fmi.sporttournament.participant.repository.ParticipantRepository;

import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.entity.category.TeamCategory;

import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.tournament.entity.category.TournamentCategory;

import com.fmi.sporttournament.user.entity.User;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantValidationService {
    private final ParticipantRepository participantRepository;

    public Participant validateUserInTeam(User user, Team team) {
        Optional<Participant> existingParticipant = participantRepository.findByUserAndTeam(user, team);
        if (existingParticipant.isEmpty()) {
            throw new ResourceNotFoundException(
                "The user with username " + user.getUsername() + " hasn't been a member of the team " + team.getName());
        }
        return existingParticipant.get();
    }

    public void validateUserAgeAndTeamCategory(User user, Team team) {
        if (user.getAge() > 18 && team.getCategory() == TeamCategory.youth) {
            throw new BusinessRuleViolationException(
                "The user " + user.getUsername() +
                    " is over 18 years old and he/she can't be added to the youth team " +
                    team.getName());
        }
    }

    public Participant validateParticipantExistById(Long id){
        Optional<Participant> participant = participantRepository.findById(id);
        if(participant.isEmpty()){
            throw new ResourceNotFoundException("The participant with id " + id + "doesn't exist");
        }
        return participant.get();
    }

    public void checkIfNoUserLeftInTeam(Team team) {
        if (participantRepository.countParticipantsByTeamAndStatus(team, ParticipantStatus.joined) == 0) {
            throw new OperationNotAllowedException("The team " + team.getName() + " isn't active");
        }
    }

    public void validateAgeAllParticipantsInTeam(Team team) {
        List<User> users = participantRepository.findUsersByTeam(team);
        for (User user : users) {
            if (user.getAge() > 18) {
                throw new BusinessRuleViolationException(
                    "The user " + user.getUsername() + " is over 18 years old");
            }
        }
    }

    public void validateTeamMemberCount(Tournament tournament, Team team) {
        Integer teamMemberCount =
            participantRepository.countParticipantsByTeamAndStatus(team, ParticipantStatus.joined);
        if (!tournament.getTeamMemberCount().equals(teamMemberCount)) {
            throw new BusinessRuleViolationException(
                "The count of the members in the team " + team.getName() + " is " + teamMemberCount +
                    ". The required count of team members for the tournament " + tournament.getTournamentName() +
                    " is " + tournament.getTeamCount());
        }
    }


    public void validateAllParticipantsInTeamWillBeInYouthCategory(Tournament tournament, Team team) {
        if (tournament.getTournamentCategory() == TournamentCategory.youth) {
            Date endAt = tournament.getEndAt();
            List<User> users = participantRepository.findUsersByTeam(team);
            for (User user : users) {
                if (user.getAgeAtDate(endAt) > 18) {
                    throw new BusinessRuleViolationException(
                        "The user with username " + user.getUsername() +
                            " will be over 18 years old until the end of the tournament " +
                            tournament.getTournamentName());
                }
            }
        }
    }
}
