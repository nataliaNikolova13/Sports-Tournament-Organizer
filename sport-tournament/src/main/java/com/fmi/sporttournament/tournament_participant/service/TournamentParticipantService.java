package com.fmi.sporttournament.tournament_participant.service;

import com.fmi.sporttournament.email.emails.tournament.TournamentEnrollmentEmail;
import com.fmi.sporttournament.exception.business.OperationNotAllowedException;
import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;

import com.fmi.sporttournament.participant.service.ParticipantValidationService;

import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.service.TeamValidationService;

import com.fmi.sporttournament.tournament.service.TournamentValidationService;
import com.fmi.sporttournament.tournament.entity.Tournament;

import com.fmi.sporttournament.tournament_participant.dto.request.TournamentParticipantRequest;
import com.fmi.sporttournament.tournament_participant.entity.TournamentParticipant;
import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;
import com.fmi.sporttournament.tournament_participant.repository.TournamentParticipantRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentParticipantService {
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final TournamentParticipantValidationService tournamentParticipantValidationService;

    private final TournamentValidationService tournamentValidationService;

    private final TeamValidationService teamValidationService;

    private final ParticipantValidationService participantValidationService;

    private final TournamentEnrollmentEmail tournamentEnrolledEmail;

    private boolean isTeamQueuedTournament(Tournament tournament, Team team) {
        return tournamentParticipantRepository.existsByTournamentIdAndTeamIdAndStatus(tournament.getId(),
            team.getId(), TournamentParticipantStatus.queued);
    }

    private boolean isTeamLeftTournament(Tournament tournament, Team team) {
        return tournamentParticipantRepository.existsByTournamentIdAndTeamIdAndStatus(tournament.getId(),
            team.getId(),
            TournamentParticipantStatus.left);
    }

    public TournamentParticipant createTournamentParticipant(Tournament tournament, Team team) {
        TournamentParticipant tournamentParticipant = new TournamentParticipant();
        tournamentParticipant.setTournament(tournament);
        tournamentParticipant.setTeam(team);
        participantValidationService.validateTeamMemberCount(tournament, team);
        if (tournamentParticipantValidationService.validateTournamentCapacity(tournament)) {
            tournamentParticipant.setStatus(TournamentParticipantStatus.queued);
            return tournamentParticipantRepository.save(tournamentParticipant);
        } else {
            tournamentParticipant.setStatus(TournamentParticipantStatus.joined);
            return tournamentParticipantRepository.save(tournamentParticipant);
        }
    }

    public TournamentParticipant changeStatus(Tournament tournament, Team team, TournamentParticipantStatus status) {
        Optional<TournamentParticipant> existingTournamentParticipant =
            tournamentParticipantRepository.findByTournamentAndTeam(tournament, team);

        if (existingTournamentParticipant.isPresent()) {
            existingTournamentParticipant.get().setStatus(status);
            existingTournamentParticipant.get().setTimeStamp(new Date());
            return tournamentParticipantRepository.save(existingTournamentParticipant.get());
        }

        throw new ResourceNotFoundException(
            "There isn't such team " + team.getName() + " in the tournament " + tournament.getTournamentName());
    }

    public TournamentParticipant rejoinTournament(Tournament tournament, Team team) {
        if (tournamentParticipantValidationService.validateTournamentCapacity(tournament)) {
            return changeStatus(tournament, team, TournamentParticipantStatus.queued);
        } else {
            return changeStatus(tournament, team, TournamentParticipantStatus.joined);
        }
    }

    public TournamentParticipant removeTournamentParticipant(Tournament tournament, Team team) {
        List<Team> queuedTeams =
            tournamentParticipantRepository.findAllTeamsByTournamentStatusAndTournament(
                TournamentParticipantStatus.queued, tournament);
        if (!queuedTeams.isEmpty() && !queuedTeams.contains(team)) {
            int i = 0;
            boolean isTeamJoined = false;
            while (i < queuedTeams.size() && !isTeamJoined) {
                try {
                    Team teamInQueue = queuedTeams.get(i);
                    tournamentParticipantValidationService.validateNoOverlappingUsersInTeamsInTournaments(teamInQueue,
                        tournament);
                    tournamentParticipantValidationService.validateTeamMemberCount(tournament, teamInQueue);

                    changeStatus(tournament, teamInQueue, TournamentParticipantStatus.joined);
                    isTeamJoined = true;
                    tournamentEnrolledEmail.sendTournamentEnrollmentEmail(teamInQueue, tournament.getTournamentName());
                } catch (OperationNotAllowedException e) {
                    i++;
                }
            }
        }
        return changeStatus(tournament, team, TournamentParticipantStatus.left);
    }

    public TournamentParticipant getTournamentParticipantById(Long id) {
        return tournamentParticipantValidationService.validateTournamentParticipantExistById(id);
    }

    public TournamentParticipant addTeamToTournament(TournamentParticipantRequest tournamentParticipantRequest) {
        Tournament tournament =
            tournamentValidationService.validateTournamentNameExist(tournamentParticipantRequest.getTournamentName());
        Team team = teamValidationService.validateTeamNameExist(tournamentParticipantRequest.getTeamName());

        tournamentParticipantValidationService.validateDateOfAdding(tournament);
        tournamentParticipantValidationService.validateSameCategory(tournament, team);
        participantValidationService.validateAllParticipantsInTeamWillBeInYouthCategory(tournament, team);
        tournamentParticipantValidationService.validateTeamMemberCount(tournament, team);

        if (tournamentParticipantValidationService.isTeamJoinedTournament(tournament, team)) {
            throw new OperationNotAllowedException(
                "Team " + team.getName() + " is already a participant of the tournament " +
                    tournament.getTournamentName());
        }

        if (isTeamQueuedTournament(tournament, team)) {
            throw new OperationNotAllowedException(
                "Team  " + team.getName() + " is already queued in the tournament " + tournament.getTournamentName());
        }

        tournamentParticipantValidationService.validateNoOverlappingUsersInTeamsInTournaments(team, tournament);

        if (isTeamLeftTournament(tournament, team)) {
            return rejoinTournament(tournament, team);
        }

        return createTournamentParticipant(tournament, team);
    }

    public TournamentParticipant deleteParticipantFromTeam(TournamentParticipantRequest tournamentParticipantRequest) {
        Tournament tournament =
            tournamentValidationService.validateTournamentNameExist(tournamentParticipantRequest.getTournamentName());
        Team team = teamValidationService.validateTeamNameExist(tournamentParticipantRequest.getTeamName());

        tournamentParticipantValidationService.validateDateOfRemoving(tournament);

        if (!tournamentParticipantRepository.existsByTournamentAndTeam(tournament, team)) {
            throw new ResourceNotFoundException(
                "Team " + team.getName() + " doesn't participate in the tournament " + tournament.getTournamentName());
        }

        if (isTeamLeftTournament(tournament, team)) {
            throw new OperationNotAllowedException(
                "Team " + team.getName() + " has already left the tournament " + tournament.getTournamentName());
        }

        return removeTournamentParticipant(tournament, team);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateParticipantsStatusAfterTournament() {
        List<TournamentParticipant> participants =
            tournamentParticipantRepository.findAllParticipantsWithStatusJoinedAndTournamentEnded(
                TournamentParticipantStatus.joined, new Date());

        for (TournamentParticipant participant : participants) {
            participant.setStatus(TournamentParticipantStatus.left);
            participant.setTimeStamp(new Date());
            tournamentParticipantRepository.save(participant);
        }
    }
}
