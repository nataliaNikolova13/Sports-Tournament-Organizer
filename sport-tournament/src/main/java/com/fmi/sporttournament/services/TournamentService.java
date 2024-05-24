package com.fmi.sporttournament.services;

import com.fmi.sporttournament.Dto.requests.tournament.TournamentCreationRequest;
import com.fmi.sporttournament.Dto.requests.tournament.TournamentRegistrationRequest;

import com.fmi.sporttournament.entity.Location;

import com.fmi.sporttournament.entity.Tournament;

import com.fmi.sporttournament.mapper.TournamentMapper;

import com.fmi.sporttournament.repositories.LocationRepository;
import com.fmi.sporttournament.repositories.TournamentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;
    private final LocationRepository locationRepository;

    private void validateTournamentNameNotExist(String tournamentName) {
        if (getTournamentByTournamentName(tournamentName).isPresent()) {
            throw new IllegalArgumentException("Tournament with this name " + tournamentName + " already exists");
        }
    }

    private Tournament validateTournamentIdExist(Long id) {
        Optional<Tournament> tournament = getTournamentById(id);
        if (tournament.isEmpty()) {
            throw new IllegalArgumentException("Tournament with this id " + id + " doesn't exist");
        }
        return tournament.get();
    }

    private Tournament validateTournamentNameExist(String tournamentName) {
        Optional<Tournament> tournament = getTournamentByTournamentName(tournamentName);
        if (tournament.isEmpty()) {
            throw new IllegalArgumentException("Tournament with this name " + tournamentName + " doesn't exist");
        }
        return tournament.get();
    }

    private Location validateLocationNameExist(String locationName) {
        Optional<Location> location = locationRepository.findByLocationName(locationName);
        if (location.isEmpty()) {
            throw new IllegalArgumentException("Location with name " + locationName + " doesn't exist");
        }
        return location.get();
    }

    private void validateTournamentNameIsNotBlank(String tournamentName) {
        if (tournamentName.isBlank()) {
            throw new IllegalArgumentException("Tournament name can't be blank");
        }
    }

    private void validateRequestDates(Date startAt, Date endAt) {
        if (endAt.toInstant().isBefore(startAt.toInstant())) {
            throw new IllegalStateException("The end date can not be before the start date of the tournament");
        }

        if (startAt.toInstant().isBefore(Instant.now())) {
            throw new IllegalStateException("The start date can not be before today date");
        }
    }

    public boolean isLocationAvailable(Long locationId, Date startAt, Date endAt) {
        List<Tournament> conflictingTournaments =
            tournamentRepository.findConflictingTournaments(locationId, startAt, endAt);
        return conflictingTournaments.isEmpty();
    }

    private void validateLocationAvailabilityDates(Location location, Date startAt, Date endAt) {
        if (!isLocationAvailable(location.getId(), startAt, endAt)) {
            throw new IllegalArgumentException(
                "Location with name " + location.getLocationName() + " is not available for the requested dates.");
        }
    }

    private void validateLocationAvailabilityDates(Tournament tournament, Location location, Date startAt, Date endAt) {
        List<Tournament> conflictingTournaments =
            tournamentRepository.findConflictingTournaments(location.getId(), startAt, endAt);
        if (!conflictingTournaments.isEmpty() &&
            (conflictingTournaments.size() != 1 || !conflictingTournaments.contains(tournament))) {
            throw new IllegalArgumentException(
                "Location with name " + location.getLocationName() + " is not available for the requested dates.");
        }
    }

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

    public Tournament createTournament(TournamentRegistrationRequest tournamentRegistrationRequest) {
        String tournamentName = tournamentRegistrationRequest.getTournamentName();

        validateTournamentNameNotExist(tournamentName);
        validateTournamentNameIsNotBlank(tournamentName);

        String locationName = tournamentRegistrationRequest.getLocationName();
        Location location = validateLocationNameExist(locationName);

        String sportType = tournamentRegistrationRequest.getSportType();

        Date startAt = tournamentRegistrationRequest.getStartAt();
        Date endAt = tournamentRegistrationRequest.getEndAt();
        validateRequestDates(startAt, endAt);
        validateLocationAvailabilityDates(location, startAt, endAt);

        TournamentCreationRequest tournamentCreationRequest =
            new TournamentCreationRequest(tournamentName, sportType, location, startAt, endAt);

        Tournament tournament = tournamentMapper.requestToTournament(tournamentCreationRequest);
        return tournamentRepository.save(tournament);
    }

    public void deleteTournamentById(Long id) {
        validateTournamentIdExist(id);
        tournamentRepository.deleteById(id);
    }

    public void deleteTournamentByTournamentName(String tournamentName) {
        Tournament tournament = validateTournamentNameExist(tournamentName);
        tournamentRepository.deleteById(tournament.getId());
    }

    public Tournament updateTournament(Long id, TournamentRegistrationRequest tournamentRegistrationRequest) {
        Tournament tournament = validateTournamentIdExist(id);

        String tournamentName = tournamentRegistrationRequest.getTournamentName();

        validateTournamentNameIsNotBlank(tournamentName);

        if (!tournament.getTournamentName().equals(tournamentName)) {
            validateTournamentNameNotExist(tournamentName);
        }

        String locationName = tournamentRegistrationRequest.getLocationName();
        Location location = validateLocationNameExist(locationName);

        String sportType = tournamentRegistrationRequest.getSportType();
        Date startAt = tournamentRegistrationRequest.getStartAt();
        Date endAt = tournamentRegistrationRequest.getEndAt();
        validateRequestDates(startAt, endAt);

        validateLocationAvailabilityDates(tournament, location, startAt, endAt);

        tournament.setTournamentName(tournamentName);
        tournament.setSportType(sportType);
        tournament.setLocation(location);
        tournament.setStartAt(startAt);
        tournament.setEndAt(endAt);

        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentNameById(Long id, String newTournamentName) {
        Tournament tournament = validateTournamentIdExist(id);

        validateTournamentNameIsNotBlank(newTournamentName);

        if (!tournament.getTournamentName().equals(newTournamentName)) {
            validateTournamentNameNotExist(newTournamentName);
        }

        tournament.setTournamentName(newTournamentName);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentNameByTournamentName(String currentTournamentName, String newTournamentName) {
        Tournament tournament = validateTournamentNameExist(currentTournamentName);

        validateTournamentNameIsNotBlank(newTournamentName);

        if (!tournament.getTournamentName().equals(newTournamentName)) {
            validateTournamentNameNotExist(newTournamentName);
        }

        tournament.setTournamentName(newTournamentName);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentLocation(Tournament tournament, String newLocationName) {
        Location location = validateLocationNameExist(newLocationName);
        tournament.setLocation(location);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentLocationById(Long id, String newLocationName) {
        Tournament tournament = validateTournamentIdExist(id);
        return updateTournamentLocation(tournament, newLocationName);
    }

    public Tournament updateTournamentLocationByTournamentName(String tournamentName, String newLocationName) {
        Tournament tournament = validateTournamentNameExist(tournamentName);
        return updateTournamentLocation(tournament, newLocationName);
    }

    public Tournament updateTournamentSportTypeById(Long id, String newSportType) {
        Tournament tournament = validateTournamentIdExist(id);
        tournament.setSportType(newSportType);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentSportTypeByTournamentName(String tournamentName, String newSportType) {
        Tournament tournament = validateTournamentNameExist(tournamentName);
        tournament.setSportType(newSportType);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentDatesById(Long id, Date newStartAt, Date newEndAt) {
        Tournament tournament = validateTournamentIdExist(id);
        validateRequestDates(newStartAt, newEndAt);
        validateLocationAvailabilityDates(tournament, tournament.getLocation(), newStartAt, newEndAt);
        tournament.setStartAt(newStartAt);
        tournament.setEndAt(newEndAt);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentDatesByTournamentName(String tournamentName, Date newStartAt, Date newEndAt) {
        Tournament tournament = validateTournamentNameExist(tournamentName);
        validateRequestDates(newStartAt, newEndAt);
        validateLocationAvailabilityDates(tournament, tournament.getLocation(), newStartAt, newEndAt);
        tournament.setStartAt(newStartAt);
        tournament.setEndAt(newEndAt);
        return tournamentRepository.save(tournament);
    }
}
