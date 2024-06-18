package com.fmi.sporttournament.tournament_participant.service;

import com.fmi.sporttournament.exception.business.OperationNotAllowedException;
import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;

import com.fmi.sporttournament.participant.repository.ParticipantRepository;

import com.fmi.sporttournament.team.entity.Team;

import com.fmi.sporttournament.tournament.entity.Tournament;

import com.fmi.sporttournament.tournament_participant.entity.TournamentParticipant;
import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;
import com.fmi.sporttournament.tournament_participant.repository.TournamentParticipantRepository;

import com.fmi.sporttournament.user.entity.User;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentParticipantValidationService {
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final ParticipantRepository participantRepository;
    public boolean isTeamJoinedTournament(Tournament tournament, Team team) {
        return tournamentParticipantRepository.existsByTournamentIdAndTeamIdAndStatus(tournament.getId(),
            team.getId(), TournamentParticipantStatus.joined);
    }

    public boolean isTeamQueuedTournament(Tournament tournament, Team team) {
        return tournamentParticipantRepository.existsByTournamentIdAndTeamIdAndStatus(tournament.getId(),
            team.getId(), TournamentParticipantStatus.queued);
    }

    public boolean isTeamLeftTournament(Tournament tournament, Team team) {
        return tournamentParticipantRepository.existsByTournamentIdAndTeamIdAndStatus(tournament.getId(),
            team.getId(),
            TournamentParticipantStatus.left);
    }

    public void validateDateOfAdding(Tournament tournament) {
        if (tournament.getStartAt().toInstant().isBefore(Instant.now())) {
            throw new OperationNotAllowedException("The team can't be enrolled after the start of the tournament");
        }
    }

    public void validateDateOfRemoving(Tournament tournament) {
        if (tournament.getStartAt().toInstant().isBefore(Instant.now())) {
            throw new OperationNotAllowedException("The team can't be removed after the beginning of the tournament");
        }
    }

    public boolean validateTournamentCapacity(Tournament tournament) {
        return tournament.getTeamCount() <=
            tournamentParticipantRepository.findAllTeamsByTournamentStatusAndTournament(
                TournamentParticipantStatus.joined, tournament).size();
    }

    public void validateSameCategory(Tournament tournament, Team team) {
        if (!tournament.getTournamentCategory().name().equals(team.getCategory().name())) {
            throw new OperationNotAllowedException(
                "The team " + team.getName() + " isn't competing in the same category");
        }
    }

    public TournamentParticipant validateTournamentParticipantExistById(Long id) {
        Optional<TournamentParticipant> tournamentParticipant = tournamentParticipantRepository.findById(id);
        if (tournamentParticipant.isEmpty()) {
            throw new ResourceNotFoundException("The tournament participant with id " + id + " doesn't exist");
        }
        return tournamentParticipant.get();
    }

    private boolean areTournamentsOverlapping(Tournament tournament1, Tournament tournament2) {
        Date start1 = tournament1.getStartAt();
        Date end1 = tournament1.getEndAt();
        Date start2 = tournament2.getStartAt();
        Date end2 = tournament2.getEndAt();

        return start1.compareTo(end2) <= 0 && start2.compareTo(end1) <= 0;
    }

    public void validateNoOverlappingUsersInTeamsInTournaments(Team team, Tournament tournament) {
        List<User> usersInTeam = participantRepository.findUsersByTeam(team);

        for (User user : usersInTeam) {
            List<Team> teamsForUser = participantRepository.findTeamsByUser(user);
            for (Team userTeam : teamsForUser) {
                List<Tournament> tournaments =
                    tournamentParticipantRepository.findTournamentsByTeamAndStatus(userTeam,
                        TournamentParticipantStatus.joined);
                for (Tournament userTeamTournament : tournaments) {
                    if (areTournamentsOverlapping(userTeamTournament, tournament)) {
                        throw new OperationNotAllowedException(
                            "Adding team " + team.getName() + "to the tournament " + tournament.getTournamentName() +
                                " conflicts with (an)other tournament(s) where the user " +
                                user.getUsername() +
                                " is participating");
                    }
                }
            }
        }
    }

    public void validateTeamNotParticipateInTournament(Team team) {
        if (!tournamentParticipantRepository.findTournamentsByTeamAndStatus(team, TournamentParticipantStatus.joined)
            .isEmpty()) {
            throw new OperationNotAllowedException("The team with name " + team.getName() +
                " already participate in tournament and it can't be modified");
        }
    }


    public void validateTournamentCapacityBeforeStart(Tournament tournament) {
        if (tournament.getTeamCount() != tournamentParticipantRepository.findAllTeamsByTournamentStatusAndTournament(
            TournamentParticipantStatus.joined, tournament).size()) {
            throw new OperationNotAllowedException(
                "The tournament can't start since the count of the joined teams isn't supported");
        }
    }

    public void validateNoTeamJoinedInTournament(Tournament tournament) {
        if (!tournamentParticipantRepository.findAllTeamsByTournamentStatusAndTournament(
            TournamentParticipantStatus.joined, tournament).isEmpty()) {
            throw new OperationNotAllowedException(
                "This change can't be executed because there are teams enrolled in the tournament");
        }
    }
}
