package com.fmi.sporttournament.participant.service;

import com.fmi.sporttournament.participant.dto.request.ParticipantRequest;

import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.team.entity.Team;

import com.fmi.sporttournament.team.entity.category.TeamCategory;
import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;
import com.fmi.sporttournament.tournament_participant.repository.TournamentParticipantRepository;

import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;

import com.fmi.sporttournament.participant.repository.ParticipantRepository;
import com.fmi.sporttournament.team.repository.TeamRepository;
import com.fmi.sporttournament.user.repository.UserRepositoty;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserRepositoty userRepositoty;
    private final TeamRepository teamRepository;
    private final TournamentParticipantRepository tournamentParticipantRepository;

    private User validateUserExist(Long id) {
        Optional<User> user = userRepositoty.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        return user.get();
    }

    private Team validateTeamExist(Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()) {
            throw new IllegalArgumentException("Team not found");
        }
        return team.get();
    }

    private void validateTeamNotParticipateInTournament(Team team) {
        if (!tournamentParticipantRepository.findTournamentsByTeamAndStatus(team, TournamentParticipantStatus.joined)
            .isEmpty()) {
            throw new IllegalArgumentException("The team already participate in tournament and it can't be modified");
        }
    }

    private Participant validateUserInTeam(User user, Team team) {
        Optional<Participant> existingParticipant = participantRepository.findByUserAndTeam(user, team);
        if (existingParticipant.isEmpty()) {
            throw new IllegalStateException("The user hasn't been a member of this team " + team.getName());
        }
        return existingParticipant.get();
    }

    private void validateUserAgeAndTeamCategory(User user, Team team) {
        if (user.getAge() > 18 && team.getCategory() == TeamCategory.youth) {
            throw new IllegalStateException(
                "The user " + user.getUsername() + " is over 18 year and he/she can't be added to the youth team " +
                    team.getCategory());
        }
    }

    private void checkIfNoUserLeftInTeam(Team team) {
        if (participantRepository.countParticipantsByTeamAndStatus(team, ParticipantStatus.joined) == 0) {
            throw new IllegalStateException("The team " + team.getName() + " isn't active");
        }
    }

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
        Participant existingParticipant = validateUserInTeam(user, team);
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

    public Participant addParticipantToTeam(ParticipantRequest participantRequest) {
        User user = validateUserExist(participantRequest.getUserId());
        Team team = validateTeamExist(participantRequest.getTeamId());

        if (isUserAlreadyInTeam(user, team)) {
            throw new IllegalStateException("User is already a participant of the team");
        }

        checkIfNoUserLeftInTeam(team);
        validateTeamNotParticipateInTournament(team);
        validateUserAgeAndTeamCategory(user, team);

        if (userAlreadyLeft(user, team)) {
            return rejoinTeam(user, team);
        }

        return create(user, team);
    }

    public Participant removeParticipantFromTeam(ParticipantRequest participantRequest) {
        User user = validateUserExist(participantRequest.getUserId());
        Team team = validateTeamExist(participantRequest.getTeamId());

        if (!participantRepository.existsByUserAndTeam(user, team)) {
            throw new IllegalStateException("User is not a member of the team");
        }
        if (userAlreadyLeft(user, team)) {
            throw new IllegalStateException("User has already left the team");
        }
        validateTeamNotParticipateInTournament(team);
        return remove(user, team);
    }

    public List<Participant> getParticipantsByTeamId(Long teamId) {
        return participantRepository.findByTeamId(teamId);
    }
}