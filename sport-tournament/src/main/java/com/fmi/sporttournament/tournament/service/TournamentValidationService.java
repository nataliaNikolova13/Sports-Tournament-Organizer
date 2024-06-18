package com.fmi.sporttournament.tournament.service;

import com.fmi.sporttournament.exception.business.BusinessRuleViolationException;
import com.fmi.sporttournament.exception.business.OperationNotAllowedException;
import com.fmi.sporttournament.exception.resource.ResourceAlreadyExistsException;
import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;

import com.fmi.sporttournament.location.entity.Location;

import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.tournament.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentValidationService {
    private final TournamentRepository tournamentRepository;

    public void validateTournamentNameNotExist(String tournamentName) {
        if (tournamentRepository.findByTournamentName(tournamentName).isPresent()) {
            throw new ResourceAlreadyExistsException(
                "Tournament with this name " + tournamentName + " already exists");
        }
    }

    public Tournament validateTournamentIdExist(Long id) {
        Optional<Tournament> tournament = tournamentRepository.findById(id);
        if (tournament.isEmpty()) {
            throw new ResourceNotFoundException("Tournament with this id " + id + " doesn't exist");
        }
        return tournament.get();
    }

    public Tournament validateTournamentNameExist(String tournamentName) {
        Optional<Tournament> tournament = tournamentRepository.findByTournamentName(tournamentName);
        if (tournament.isEmpty()) {
            throw new ResourceNotFoundException("Tournament with this name " + tournamentName + " doesn't exist");
        }
        return tournament.get();
    }

    public void validateRequestDates(Date startAt, Date endAt) {
        if (endAt.toInstant().isBefore(startAt.toInstant())) {
            throw new OperationNotAllowedException("The end date can not be before the start date of the tournament");
        }
    }

    public void validateDateOfUpdate(Date startAt) {
        if (startAt.toInstant().isBefore(Instant.now())) {
            throw new OperationNotAllowedException("The tournament can't be changed during the competition");
        }
    }

    private boolean isLocationAvailable(Long locationId, Date startAt, Date endAt) {
        List<Tournament> conflictingTournaments =
            tournamentRepository.findConflictingTournaments(locationId, startAt, endAt);
        return conflictingTournaments.isEmpty();
    }

    public void validateLocationAvailabilityDates(Location location, Date startAt, Date endAt) {
        if (!isLocationAvailable(location.getId(), startAt, endAt)) {
            throw new OperationNotAllowedException(
                "Location with name " + location.getLocationName() + " is not available for the requested dates.");
        }
    }


    public void validateLocationAvailabilityDates(Tournament tournament, Location location, Date startAt, Date endAt) {
        List<Tournament> conflictingTournaments =
            tournamentRepository.findConflictingTournaments(location.getId(), startAt, endAt);
        if (!conflictingTournaments.isEmpty() &&
            (conflictingTournaments.size() != 1 || !conflictingTournaments.contains(tournament))) {
            throw new OperationNotAllowedException(
                "Location with name " + location.getLocationName() + " is not available for the requested dates.");
        }
    }

    public void validateTeamCount(Integer teamCount) {
        if ((teamCount & (teamCount - 1)) != 0) {
            throw new OperationNotAllowedException("The team count should be power of 2.");
        }
    }

    public void validateHours(Integer startHour, Integer endHour) {
        if (startHour >= endHour) {
            throw new OperationNotAllowedException("The start hour can't be after the end hour");
        }

        if (startHour == 0 && endHour == 24) {
            throw new OperationNotAllowedException("The match duration can't be 24 hours");
        }
    }

    public void validateMatchDuration(Integer startHour, Integer endHour, Integer matchDuration) {
        if (matchDuration > (endHour - startHour)) {
            throw new BusinessRuleViolationException(
                "The match duration " + matchDuration +
                    " hour(s) can't be more than the duration of the whole tournament day - " + (endHour - startHour) +
                    " hour(s)");
        }
    }

    public void validateLocationIsNotUsedInTournament(Location location) {
        if (!tournamentRepository.findValidTournamentsByLocation(location).isEmpty()) {
            throw new OperationNotAllowedException("The location with name " + location.getLocationName() +
                " is used for a tournament and its venues can't be changed");
        }
    }
}
