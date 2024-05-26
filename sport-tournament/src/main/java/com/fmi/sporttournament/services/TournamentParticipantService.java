package com.fmi.sporttournament.services;

import com.fmi.sporttournament.Dto.requests.tournament.TournamentParticipantRequest;
import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.TournamentParticipant;

import com.fmi.sporttournament.entity.enums.TournamentParticipantStatus;

import com.fmi.sporttournament.repositories.TeamRepository;
import com.fmi.sporttournament.repositories.TournamentParticipantRepository;
import com.fmi.sporttournament.repositories.TournamentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentParticipantService {
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    public TournamentParticipant create(Tournament tournament, Team team) {
        TournamentParticipant tournamentParticipant = new TournamentParticipant();
        tournamentParticipant.setTournament(tournament);
        tournamentParticipant.setTeam(team);
        tournamentParticipant.setStatus(TournamentParticipantStatus.joined);
        return tournamentParticipantRepository.save(tournamentParticipant);
    }

    public TournamentParticipant changeStatus(Tournament tournament, Team team, TournamentParticipantStatus status) {
        Optional<TournamentParticipant> existingTournamentParticipant =
            tournamentParticipantRepository.findByTournamentAndTeam(tournament, team);

        if (existingTournamentParticipant.isPresent()) {
            existingTournamentParticipant.get().setStatus(status);
            existingTournamentParticipant.get().setTimeStamp(new Date());
            return tournamentParticipantRepository.save(existingTournamentParticipant.get());
        }

        return null;
    }

    public TournamentParticipant rejoinTournament(Tournament tournament, Team team) {
        return changeStatus(tournament, team, TournamentParticipantStatus.joined);
    }

    public TournamentParticipant remove(Tournament tournament, Team team) {
        return changeStatus(tournament, team, TournamentParticipantStatus.left);
    }

    public boolean isTeamAlreadyInTournament(Tournament tournament, Team team) {
        return tournamentParticipantRepository.existsByTournamentIdAndTeamIdAndStatus(tournament.getId(),
            team.getId(),
            TournamentParticipantStatus.joined);
    }

    public boolean teamAlreadyLeft(Tournament tournament, Team team) {
        return tournamentParticipantRepository.existsByTournamentIdAndTeamIdAndStatus(tournament.getId(),
            team.getId(),
            TournamentParticipantStatus.left);
    }

    private Tournament validateRequestTournament(TournamentParticipantRequest tournamentParticipantRequest) {
        String tournamentName = tournamentParticipantRequest.getTournamentName();

        Optional<Tournament> tournament = tournamentRepository.findByTournamentName(tournamentName);

        if (tournament.isEmpty()) {
            throw new IllegalArgumentException("Tournament with this name doesn't exist");
        }

        return tournament.get();
    }

    private Team validateRequestTeam(TournamentParticipantRequest tournamentParticipantRequest) {
        String teamName = tournamentParticipantRequest.getTeamName();

        Optional<Team> team = teamRepository.findByName(teamName);

        if (team.isEmpty()) {
            throw new IllegalArgumentException("Team with this name doesn't exist");
        }

        return team.get();
    }

    public TournamentParticipant addTeamToTournament(TournamentParticipantRequest tournamentParticipantRequest) {
        Tournament tournament = validateRequestTournament(tournamentParticipantRequest);
        Team team = validateRequestTeam(tournamentParticipantRequest);

        if (isTeamAlreadyInTournament(tournament, team)) {
            throw new IllegalStateException("Team is already a participant of the tournament");
        }

        if (teamAlreadyLeft(tournament, team)) {
            return rejoinTournament(tournament, team);
        }

        return create(tournament, team);
    }

    public TournamentParticipant removeParticipantFromTeam(TournamentParticipantRequest tournamentParticipantRequest) {
        Tournament tournament = validateRequestTournament(tournamentParticipantRequest);
        Team team = validateRequestTeam(tournamentParticipantRequest);

        if (!tournamentParticipantRepository.existsByTournamentAndTeam(tournament, team)) {
            throw new IllegalStateException("Team doesn't participate in the tournament");
        }

        if (teamAlreadyLeft(tournament, team)) {
            throw new IllegalStateException("Team has already left the tournament");
        }

        return remove(tournament, team);
    }
}