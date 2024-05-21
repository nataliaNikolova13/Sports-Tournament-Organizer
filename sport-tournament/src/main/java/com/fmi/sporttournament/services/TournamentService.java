package com.fmi.sporttournament.services;

import com.fmi.sporttournament.Dto.requests.tournament.TournamentCreationRequest;
import com.fmi.sporttournament.Dto.requests.tournament.TournamentRegistrationRequest;

import com.fmi.sporttournament.entity.Location;

import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.entity.Tournament;

import com.fmi.sporttournament.mapper.TournamentMapper;

import com.fmi.sporttournament.repositories.LocationRepository;
import com.fmi.sporttournament.repositories.RoundRepository;
import com.fmi.sporttournament.repositories.TeamRepository;
import com.fmi.sporttournament.repositories.TournamentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;
    private final LocationRepository locationRepository;
    private final TeamRepository teamRepository;
    private final RoundRepository roundRepository;

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentRepository.findById(id);
    }

    public Optional<Tournament> getTournamentByTournamentName(String tournamentName) {
        return tournamentRepository.findByTournamentName(tournamentName);
    }

    public List<Tournament> getTournamentBySportType(String sportType) {
        return tournamentRepository.findBySportType(sportType);
    }

    public List<Tournament> getTournamentByLocationName(String locationName) {
        return tournamentRepository.findByLocationLocationName(locationName);
    }

    public void removeTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

    private Location validateRequestLocation(TournamentRegistrationRequest tournamentRegistrationRequest) {
        Optional<Location> location =
            locationRepository.findByLocationName(tournamentRegistrationRequest.getLocationName());

        if (location.isEmpty()) {
            throw new IllegalArgumentException("Location not found");
        }
        return location.get();
    }

    private void validateRequestDates(Date startAt, Date endAt) {
        if (endAt.toInstant().isBefore(startAt.toInstant())) {
            throw new IllegalStateException("The end date can not be before the start date of the tournament");
        }

        if (startAt.toInstant().isBefore(Instant.now())) {
            throw new IllegalStateException("The start date can not be before the today");
        }
    }

    public Tournament createTournament(TournamentRegistrationRequest tournamentRegistrationRequest) {
        String tournamentName = tournamentRegistrationRequest.getTournamentName();

        if (getTournamentByTournamentName(tournamentName).isPresent()) {
            throw new IllegalArgumentException("Tournament with this name already exists");
        }
        Location location = validateRequestLocation(tournamentRegistrationRequest);
        String sportType = tournamentRegistrationRequest.getSportType();
        Date startAt = tournamentRegistrationRequest.getStartAt();
        Date endAt = tournamentRegistrationRequest.getEndAt();
        validateRequestDates(startAt,endAt);
        TournamentCreationRequest tournamentCreationRequest =
            new TournamentCreationRequest(tournamentName, sportType, location, startAt, endAt);

        Tournament tournament = tournamentMapper.requestToTournament(tournamentCreationRequest);
        return tournamentRepository.save(tournament);
    }

    public void startTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        List<Team> teams = teamRepository.findTeamsByTournamentId(tournamentId);
        Collections.shuffle(teams);
    }

//    public void processNextRound(Long tournamentId) {
//        Tournament tournament = tournamentRepository.findById(tournamentId)
//                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
//
//        int nextRoundNumber = roundRepository.findByTournamentId(tournamentId).get().getRoundNumber() + 1;
//        List<Team> winningTeams = getWinningTeamsFromLastRound(tournamentId, nextRoundNumber - 1);
//    }
}
