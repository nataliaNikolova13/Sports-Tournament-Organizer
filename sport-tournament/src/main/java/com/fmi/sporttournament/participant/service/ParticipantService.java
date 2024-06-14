package com.fmi.sporttournament.participant.service;

import com.fmi.sporttournament.participant.dto.request.ParticipantRequest;

import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.team.entity.Team;

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

    private void validateTeamNotParticipateInTournament(Team team) {
        if (!tournamentParticipantRepository.findTournamentsByTeam(team, TournamentParticipantStatus.joined)
            .isEmpty()) {
            throw new IllegalArgumentException("The team already participate in tournament and it can't be modified");
        }
    }

    private Participant validateUserInTeam(User user, Team team) {
        Optional<Participant> existingParticipant = participantRepository.findByUserAndTeam(user, team);
        if (existingParticipant.isEmpty()) {
            throw new IllegalStateException("The user hasn't been a member of this team");
        }
        return existingParticipant.get();
    }

    private void checkIfNoUserLeftInTeam(Team team) {
        if (participantRepository.countParticipantsByTeamAndStatus(team, ParticipantStatus.joined) == 0) {
            throw new IllegalStateException("The team " + team.getName() + " isn't active");
        }
    }

    public Participant create(User user, Team team) {
        Participant participant = new Participant();
        validateTeamNotParticipateInTournament(team);
        participant.setUser(user);
        participant.setTeam(team);
        participant.setStatus(ParticipantStatus.joined);
        return participantRepository.save(participant);
    }

    public Participant remove(User user, Team team) {
        return changeStatus(user, team, ParticipantStatus.left);
    }

    public Participant rejoinTeam(User user, Team team) {
        return changeStatus(user, team, ParticipantStatus.joined);
    }

    private Participant changeStatus(User user, Team team, ParticipantStatus status) {
        Participant existingParticipant = validateUserInTeam(user, team);
        existingParticipant.setStatus(status);
        existingParticipant.setTimeStamp(new Date());
        return participantRepository.save(existingParticipant);
    }

    public boolean isUserAlreadyInTeam(User user, Team team) {
        return participantRepository.existsByUserIdAndTeamIdAndStatus(user.getId(), team.getId(),
            ParticipantStatus.joined);
    }

    public boolean userAlreadyLeft(User user, Team team) {
        return participantRepository.existsByUserIdAndTeamIdAndStatus(user.getId(), team.getId(),
            ParticipantStatus.left);
    }

    public Participant addParticipantToTeam(ParticipantRequest participantRequest) {
        Optional<User> user = userRepositoty.findById(participantRequest.getUserId());
        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        Optional<Team> team = teamRepository.findById(participantRequest.getTeamId());
        if (!team.isPresent()) {
            throw new IllegalArgumentException("Team not found");
        }
        if (isUserAlreadyInTeam(user.get(), team.get())) {
            throw new IllegalStateException("User is already a participant of the team");
        }

        checkIfNoUserLeftInTeam(team.get());
        validateTeamNotParticipateInTournament(team.get());
        if (userAlreadyLeft(user.get(), team.get())) {
            return rejoinTeam(user.get(), team.get());
        }
        return create(user.get(), team.get());
    }

    public Participant removeParticipantFromTeam(ParticipantRequest participantRequest) {
        Optional<User> user = userRepositoty.findById(participantRequest.getUserId());
        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        Optional<Team> team = teamRepository.findById(participantRequest.getTeamId());
        if (!team.isPresent()) {
            throw new IllegalArgumentException("Team not found");
        }
        if (!participantRepository.existsByUserAndTeam(user.get(), team.get())) {
            throw new IllegalStateException("User is not a member of the team");
        }
        if (userAlreadyLeft(user.get(), team.get())) {
            throw new IllegalStateException("User has already left the team");
        }
        validateTeamNotParticipateInTournament(team.get());
        return remove(user.get(), team.get());
    }
}