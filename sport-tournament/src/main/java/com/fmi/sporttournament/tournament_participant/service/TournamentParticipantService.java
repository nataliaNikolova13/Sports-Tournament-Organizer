package com.fmi.sporttournament.tournament_participant.service;

import com.fmi.sporttournament.email.emails.tournament.TournamentEnrollmentEmail;
import com.fmi.sporttournament.exception.business.OperationNotAllowedException;
import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;

import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;
import com.fmi.sporttournament.participant.repository.ParticipantRepository;
import com.fmi.sporttournament.participant.service.ParticipantValidationService;

import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.repository.TeamRepository;
import com.fmi.sporttournament.team.service.TeamValidationService;

import com.fmi.sporttournament.tournament.entity.category.TournamentCategory;
import com.fmi.sporttournament.tournament.repository.TournamentRepository;
import com.fmi.sporttournament.tournament.service.TournamentValidationService;
import com.fmi.sporttournament.tournament.entity.Tournament;

import com.fmi.sporttournament.tournament_participant.dto.request.TournamentParticipantRequest;
import com.fmi.sporttournament.tournament_participant.entity.TournamentParticipant;
import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;
import com.fmi.sporttournament.tournament_participant.repository.TournamentParticipantRepository;

import com.fmi.sporttournament.user.entity.User;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;

    private boolean isTeamQueuedTournament(Tournament tournament, Team team) {
        return tournamentParticipantRepository.existsByTournamentIdAndTeamIdAndStatus(tournament.getId(),
            team.getId(), TournamentParticipantStatus.queued);
    }

    private boolean isTeamLeftTournament(Tournament tournament, Team team) {
        return tournamentParticipantRepository.existsByTournamentIdAndTeamIdAndStatus(tournament.getId(),
            team.getId(),
            TournamentParticipantStatus.left);
    }

    private Tournament validateTournamentNameExist(TournamentParticipantRequest tournamentParticipantRequest) {
        String tournamentName = tournamentParticipantRequest.getTournamentName();

        Optional<Tournament> tournament = tournamentRepository.findByTournamentName(tournamentName);

        if (tournament.isEmpty()) {
            throw new IllegalArgumentException("Tournament with name " + tournamentName + "doesn't exist");
        }

        return tournament.get();
    }

    private Team validateTeamNameExist(TournamentParticipantRequest tournamentParticipantRequest) {
        String teamName = tournamentParticipantRequest.getTeamName();

        Optional<Team> team = teamRepository.findByName(teamName);

        if (team.isEmpty()) {
            throw new IllegalArgumentException("Team with name " + teamName + "doesn't exist");
        }

        return team.get();
    }

    private void validateDateOfAdding(Tournament tournament) {
        if (tournament.getStartAt().toInstant().isBefore(Instant.now())) {
            throw new IllegalStateException("The team can't be enrolled after the start of the tournament");
        }
    }

    private void validateDateOfRemoving(Tournament tournament) {
        if (tournament.getStartAt().toInstant().isBefore(Instant.now())) {
            throw new IllegalStateException("The team can't be removed after the beginning of the tournament");
        }
    }

    private boolean validateTournamentCapacity(Tournament tournament) {
        return tournament.getTeamCount() <=
            tournamentParticipantRepository.findAllTeamsByTournamentStatusAndTournament(
                TournamentParticipantStatus.joined, tournament).size();
    }

    private void validateTeamMemberCount(Tournament tournament, Team team) {
        int teamMemberCount =
            participantRepository.countParticipantsByTeamAndStatus(team, ParticipantStatus.joined);
        System.out.println(tournament.getTeamMemberCount());
        System.out.println(teamMemberCount);
        System.out.println(tournament.getTeamMemberCount() == teamMemberCount);
        if (tournament.getTeamMemberCount() != teamMemberCount) {
            throw new IllegalArgumentException(
                "The count of the members in the team isn't supported for the tournament");
        }
    }

    private void validateSameCategory(Tournament tournament, Team team) {
        if (!tournament.getTournamentCategory().name().equals(team.getCategory().name())) {
            throw new IllegalStateException("The team " + team.getName() + " isn't competing in the same category");
        }
    }

    private void validateAllParticipantsInTeamWillBeInYouthCategory(Tournament tournament, Team team) {
        if (tournament.getTournamentCategory() == TournamentCategory.youth) {
            Date endAt = tournament.getEndAt();
            List<User> users = participantRepository.findUsersByTeam(team);
            for (User user : users) {
                if (user.getAgeAtDate(endAt) > 18) {
                    throw new IllegalStateException(
                        "The user " + user.getUsername() + " will be over 18 until the end of the tournament.");
                }
            }
        }
    }

    private void validateNoOverlappingUsersInTeamsInTournaments(Team team, Tournament tournament) {
        List<User> usersInTeam = participantRepository.findUsersByTeam(team);

        for (User user : usersInTeam) {
            List<Team> teamsForUser = participantRepository.findTeamsByUser(user);
            for (Team userTeam : teamsForUser) {
                List<Tournament> tournaments =
                    tournamentParticipantRepository.findTournamentsByTeamAndStatus(userTeam,
                        TournamentParticipantStatus.joined);
                for (Tournament userTeamTournament : tournaments) {
                    if (areTournamentsOverlapping(userTeamTournament, tournament)) {
                        throw new IllegalStateException(
                            "Adding team conflicts with another tournament where the user " + user.getUsername() +
                                " is participating.");
                    }
                }
            }
        }
    }

    private boolean areTournamentsOverlapping(Tournament tournament1, Tournament tournament2) {
        Date start1 = tournament1.getStartAt();
        Date end1 = tournament1.getEndAt();
        Date start2 = tournament2.getStartAt();
        Date end2 = tournament2.getEndAt();

        return start1.compareTo(end2) <= 0 && start2.compareTo(end1) <= 0;
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

    public List<TournamentParticipant> getParticipantsByTournament(Tournament tournament) {
        return tournamentParticipantRepository.findByTournament(tournament);
    }
}
