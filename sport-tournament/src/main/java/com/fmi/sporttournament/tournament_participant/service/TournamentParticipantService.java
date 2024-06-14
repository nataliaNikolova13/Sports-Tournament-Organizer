package com.fmi.sporttournament.tournament_participant.service;

import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;
import com.fmi.sporttournament.participant.repository.ParticipantRepository;
import com.fmi.sporttournament.tournament_participant.dto.request.TournamentParticipantRequest;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.tournament_participant.entity.TournamentParticipant;

import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;

import com.fmi.sporttournament.team.repository.TeamRepository;
import com.fmi.sporttournament.tournament_participant.repository.TournamentParticipantRepository;
import com.fmi.sporttournament.tournament.repository.TournamentRepository;

import com.fmi.sporttournament.user.entity.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentParticipantService {
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;

    private boolean isTeamJoinedTournament(Tournament tournament, Team team) {
        return tournamentParticipantRepository.existsByTournamentIdAndTeamIdAndStatus(tournament.getId(),
            team.getId(), TournamentParticipantStatus.joined);
    }

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
        Integer teamMemberCount =
            participantRepository.countParticipantsByTeamAndStatus(team, ParticipantStatus.joined);
        if (!tournament.getTeamMemberCount().equals(teamMemberCount)) {
            throw new IllegalArgumentException("The count of the members isn't supported for the tournament");
        }
    }

    private void validateNoOverlappingUsersInTeamsInTournaments(Team team, Tournament tournament) {
        List<User> usersInTeam = participantRepository.findUsersByTeam(team);

        for (User user : usersInTeam) {
            List<Team> teamsForUser = participantRepository.findTeamsByUser(user);
            for (Team userTeam : teamsForUser) {
                List<Tournament> tournaments =
                    tournamentParticipantRepository.findTournamentsByTeam(userTeam, TournamentParticipantStatus.joined);
                for (Tournament userTeamTournament : tournaments) {
                    if (areTournamentsOverlapping(userTeamTournament, tournament)) {
                        throw new IllegalStateException(
                            "Adding team conflicts with another tournament where the user " + user.getFullName() +
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
        validateTeamMemberCount(tournament, team);
        if (validateTournamentCapacity(tournament)) {
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

        throw new IllegalStateException("There isn't such team in the tournament.");
    }

    public TournamentParticipant rejoinTournament(Tournament tournament, Team team) {
        if (validateTournamentCapacity(tournament)) {
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
                    // to do send email
                    validateNoOverlappingUsersInTeamsInTournaments(teamInQueue, tournament);
                    changeStatus(tournament, teamInQueue, TournamentParticipantStatus.joined);
                    isTeamJoined = true;
                } catch (IllegalStateException e) {
                    i++;
                }
            }
        }
        return changeStatus(tournament, team, TournamentParticipantStatus.left);
    }

    public TournamentParticipant addTeamToTournament(TournamentParticipantRequest tournamentParticipantRequest) {
        Tournament tournament = validateTournamentNameExist(tournamentParticipantRequest);
        Team team = validateTeamNameExist(tournamentParticipantRequest);

        if (isTeamJoinedTournament(tournament, team)) {
            throw new IllegalStateException("Team is already a participant of the tournament");
        }

        if (isTeamQueuedTournament(tournament, team)) {
            throw new IllegalStateException("Team is already queued in the tournament");
        }

        validateDateOfAdding(tournament);
        validateNoOverlappingUsersInTeamsInTournaments(team, tournament);

        if (isTeamLeftTournament(tournament, team)) {
            return rejoinTournament(tournament, team);
        }

        return createTournamentParticipant(tournament, team);
    }

    public TournamentParticipant deleteParticipantFromTeam(TournamentParticipantRequest tournamentParticipantRequest) {
        Tournament tournament = validateTournamentNameExist(tournamentParticipantRequest);
        Team team = validateTeamNameExist(tournamentParticipantRequest);

        if (!tournamentParticipantRepository.existsByTournamentAndTeam(tournament, team)) {
            throw new IllegalStateException("Team doesn't participate in the tournament");
        }

        if (isTeamLeftTournament(tournament, team)) {
            throw new IllegalStateException("Team has already left the tournament");
        }

        validateDateOfRemoving(tournament);

        return removeTournamentParticipant(tournament, team);
    }
}
