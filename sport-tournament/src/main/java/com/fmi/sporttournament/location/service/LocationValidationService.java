package com.fmi.sporttournament.location.service;

import com.fmi.sporttournament.exception.business.OperationNotAllowedException;
import com.fmi.sporttournament.exception.resource.ResourceAlreadyExistsException;
import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;

import com.fmi.sporttournament.location.entity.Location;
import com.fmi.sporttournament.location.repository.LocationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationValidationService {
    private final LocationRepository locationRepository;
    public void validateLocationNameNotExist(String locationName) {
        if (locationRepository.findByLocationName(locationName).isPresent()) {
            throw new ResourceAlreadyExistsException("Location with the name " + locationName + " already exists");
        }
    }

    public Location validateLocationIdExist(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isEmpty()) {
            throw new ResourceNotFoundException("Location with this id " + id + " doesn't exist");
        }
        return location.get();
    }

    public Location validateLocationNameExist(String locationName) {
        Optional<Location> location = locationRepository.findByLocationName(locationName);
        if (location.isEmpty()) {
            throw new ResourceNotFoundException("Location with the name " + locationName + " doesn't exist");
        }
        return location.get();
    }

    private Integer matchesInVenuePerDay(Integer startHour, Integer endHour, Integer matchDuration) {
        Integer durationTournamentDay = endHour - startHour;
        return durationTournamentDay / matchDuration;
    }

    private Integer howManyDaysForOneRound(Integer matchesCountInRound, Long matchesPerDay) {
        return (int) Math.ceil((double) matchesCountInRound / matchesPerDay);
    }

    public void validateVenuesAvailability(String locationName, Date startAt, Date endAt, Integer startHour,
                                            Integer endHour, Integer teamCount, Integer matchDuration) {
        Long availableDays = ChronoUnit.DAYS.between(startAt.toInstant(), endAt.toInstant());
        Long venueCount = locationRepository.countVenuesByLocationName(locationName);
        Integer matchesInVenuePerDay = matchesInVenuePerDay(startHour, endHour, matchDuration);
        Long matchesPerDay = matchesInVenuePerDay * venueCount;
        Integer teamCountLeftForTheRound = teamCount;
        int necessaryDays = 0;
        while (teamCountLeftForTheRound > 0) {
            Integer matchesCountInRound = teamCountLeftForTheRound / 2;
            necessaryDays += howManyDaysForOneRound(matchesCountInRound, matchesPerDay);
            teamCountLeftForTheRound /= 2;
        }
        if (necessaryDays > availableDays) {
            throw new OperationNotAllowedException("There aren't enough venues for this count of teams " + teamCount +
                " and for this match duration " + matchDuration + " hour(s) and the requested datesc");
        }
    }
}
