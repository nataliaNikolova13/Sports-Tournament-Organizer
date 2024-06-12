package com.fmi.sporttournament.tournament.service;

import com.fmi.sporttournament.tournament.dto.request.TournamentCreationRequest;
import com.fmi.sporttournament.tournament.dto.request.TournamentRegistrationRequest;

import com.fmi.sporttournament.location.entity.Location;

import com.fmi.sporttournament.tournament.entity.Tournament;

import com.fmi.sporttournament.tournament.mapper.TournamentMapper;

import com.fmi.sporttournament.location.repository.LocationRepository;
import com.fmi.sporttournament.round.repository.RoundRepository;
import com.fmi.sporttournament.team.repository.TeamRepository;
import com.fmi.sporttournament.tournament.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private void validateTeamCount(Integer teamCount) {
        if (teamCount < 0 || (teamCount & (teamCount - 1)) != 0) {
            throw new IllegalArgumentException("The team count should be power of 2.");
        }
    }

    private void validateHours(Integer startHour, Integer endHour) {
        if (startHour < 0 || startHour > 24) {
            throw new IllegalArgumentException("The start hour is invalid");
        }

        if (endHour < 0 || endHour > 24) {
            throw new IllegalArgumentException("The end date is invalid");
        }

        if (startHour >= endHour) {
            throw new IllegalArgumentException("The start hour can't be after the end hour");
        }
    }

    private void validateMatchDuration(Integer startHour, Integer endHour, Integer matchDuration) {
        if (matchDuration < 0 || matchDuration > (endHour - startHour)) {
            throw new IllegalArgumentException(
                "The match duration can't be more than the duration of the whole tournament day");
        }
    }

    private Integer matchCount(Integer teamCount) {
        return teamCount - 1;
    }

    private Integer roundsInVenuePerDay(Integer startHour, Integer endHour, Integer matchDuration) {
        Integer durationTournamentDay = endHour - startHour;
        return durationTournamentDay / matchDuration;
    }

    private void validateVenuesAvailability(String locationName, Date startAt, Date endAt, Integer startHour,
                                            Integer endHour, Integer teamCount, Integer matchDuration) {
        Long availableDays = ChronoUnit.DAYS.between(startAt.toInstant(), endAt.toInstant());
        Long venueCount = locationRepository.countVenuesByLocationName(locationName);
        Integer roundsInVenuePerDay = roundsInVenuePerDay(startHour, endHour, matchDuration);
        Integer possibleMatchCount = Math.toIntExact((int) roundsInVenuePerDay * venueCount * availableDays);
        Integer matchCount = matchCount(teamCount);

        if (possibleMatchCount < matchCount) {
            throw new IllegalArgumentException("There aren't enough venues for this count of teams " + teamCount +
                " and for this match duration in hours " + matchDuration + ".");
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

        Integer startHour = tournamentRegistrationRequest.getStartHour();
        Integer endHour = tournamentRegistrationRequest.getEndHour();
        validateHours(startHour, endHour);

        Integer teamCount = tournamentRegistrationRequest.getTeamCount();
        validateTeamCount(teamCount);

        Integer matchDuration = tournamentRegistrationRequest.getMatchDuration();
        validateMatchDuration(startHour, endHour, matchDuration);

        validateVenuesAvailability(locationName, startAt, endAt, startHour,
            endHour, teamCount, matchDuration);

        TournamentCreationRequest tournamentCreationRequest =
            new TournamentCreationRequest(tournamentName, sportType, location, startAt, endAt, startHour, endHour,
                teamCount, matchDuration);

        Tournament tournament = tournamentMapper.requestToTournament(tournamentCreationRequest);
        return tournamentRepository.save(tournament);
    }

//    public void startTournament(Long tournamentId) {
//        Tournament tournament = tournamentRepository.findById(tournamentId)
//                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
//        List<Team> teams = teamRepository.findTeamsByTournamentId(tournamentId);
//        Collections.shuffle(teams);
//    }

//    public void processNextRound(Long tournamentId) {
//        Tournament tournament = tournamentRepository.findById(tournamentId)
//                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
//
//        int nextRoundNumber = roundRepository.findByTournamentId(tournamentId).get().getRoundNumber() + 1;
//        List<Team> winningTeams = getWinningTeamsFromLastRound(tournamentId, nextRoundNumber - 1);
//    }

    public void deleteTournamentById(Long id) {
        validateTournamentIdExist(id);
        tournamentRepository.deleteById(id);
    }

    public void deleteTournamentByTournamentName(String tournamentName) {
        Tournament tournament = validateTournamentNameExist(tournamentName);
        tournamentRepository.deleteById(tournament.getId());
    }

    private void setTournament(Tournament tournament, String tournamentName, String sportType, Location location,
                               Date startAt, Date endAt,
                               Integer startHour, Integer endHour, Integer teamCount, Integer matchDuration) {
        tournament.setTournamentName(tournamentName);
        tournament.setSportType(sportType);
        tournament.setLocation(location);
        tournament.setStartAt(startAt);
        tournament.setEndAt(endAt);
        tournament.setStartHour(startHour);
        tournament.setEndHour(endHour);
        tournament.setTeamCount(teamCount);
        tournament.setMatchDuration(matchDuration);
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

        Integer startHour = tournamentRegistrationRequest.getStartHour();
        Integer endHour = tournamentRegistrationRequest.getEndHour();
        validateHours(startHour, endHour);

        Integer teamCount = tournamentRegistrationRequest.getTeamCount();
        validateTeamCount(teamCount);

        Integer matchDuration = tournamentRegistrationRequest.getMatchDuration();
        validateMatchDuration(startHour, endHour, matchDuration);

        validateVenuesAvailability(locationName, startAt, endAt, startHour,
            endHour, teamCount, matchDuration);

        setTournament(tournament, tournamentName, sportType, location, startAt, endAt, startHour, endHour, teamCount,
            matchDuration);

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

    private Tournament updateTournamentLocation(Tournament tournament, String newLocationName) {
        Location location = validateLocationNameExist(newLocationName);

        Date startAt = tournament.getStartAt();
        Date endAt = tournament.getEndAt();

        validateLocationAvailabilityDates(tournament, location, startAt, endAt);

        Integer startHour = tournament.getStartHour();
        Integer endHour = tournament.getEndHour();
        Integer teamCount = tournament.getTeamCount();
        Integer matchDuration = tournament.getMatchDuration();

        validateVenuesAvailability(location.getLocationName(), startAt, endAt, startHour,
            endHour, teamCount, matchDuration);

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

    private Tournament updateTournamentDates(Tournament tournament, Date newStartAt, Date newEndAt) {
        validateRequestDates(newStartAt, newEndAt);

        Location location = tournament.getLocation();
        validateLocationAvailabilityDates(tournament, location, newStartAt, newEndAt);

        Integer startHour = tournament.getStartHour();
        Integer endHour = tournament.getEndHour();
        Integer teamCount = tournament.getTeamCount();
        Integer matchDuration = tournament.getMatchDuration();

        validateVenuesAvailability(location.getLocationName(), newStartAt, newEndAt, startHour,
            endHour, teamCount, matchDuration);

        tournament.setStartAt(newStartAt);
        tournament.setEndAt(newEndAt);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentDatesById(Long id, Date newStartAt, Date newEndAt) {
        Tournament tournament = validateTournamentIdExist(id);
        return updateTournamentDates(tournament, newStartAt, newEndAt);
    }

    public Tournament updateTournamentDatesByTournamentName(String tournamentName, Date newStartAt, Date newEndAt) {
        Tournament tournament = validateTournamentNameExist(tournamentName);
        return updateTournamentDates(tournament, newStartAt, newEndAt);
    }

    private Tournament updateTournamentMatchDuration(Tournament tournament, Integer newMatchDuration) {
        String locationName = tournament.getLocation().getLocationName();
        Date startAt = tournament.getStartAt();
        Date endAt = tournament.getEndAt();
        Integer startHour = tournament.getStartHour();
        Integer endHour = tournament.getEndHour();
        Integer teamCount = tournament.getTeamCount();
        validateMatchDuration(startHour, endHour, newMatchDuration);

        validateVenuesAvailability(locationName, startAt, endAt, startHour,
            endHour, teamCount, newMatchDuration);

        tournament.setMatchDuration(newMatchDuration);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentMatchDurationById(Long id, Integer newMatchDuration) {
        Tournament tournament = validateTournamentIdExist(id);
        return updateTournamentMatchDuration(tournament, newMatchDuration);
    }

    public Tournament updateTournamentMatchDurationByTournamentName(String tournamentName, Integer newMatchDuration) {
        Tournament tournament = validateTournamentNameExist(tournamentName);
        return updateTournamentMatchDuration(tournament, newMatchDuration);
    }

    private Tournament updateTournamentHours(Tournament tournament, Integer newStartHour, Integer newEndHour) {
        String locationName = tournament.getLocation().getLocationName();
        Date startAt = tournament.getStartAt();
        Date endAt = tournament.getEndAt();

        validateHours(newStartHour, newEndHour);

        Integer teamCount = tournament.getTeamCount();

        Integer matchDuration = tournament.getMatchDuration();
        validateMatchDuration(newStartHour, newEndHour, matchDuration);

        validateVenuesAvailability(locationName, startAt, endAt, newStartHour,
            newEndHour, teamCount, matchDuration);

        tournament.setStartHour(newStartHour);
        tournament.setEndHour(newEndHour);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentHoursById(Long id, Integer newStartHour, Integer newEndHour) {
        Tournament tournament = validateTournamentIdExist(id);
        return updateTournamentHours(tournament, newStartHour, newEndHour);
    }

    public Tournament updateTournamentHoursByTournamentName(String tournamentName, Integer newStartHour,
                                                            Integer newEndHour) {
        Tournament tournament = validateTournamentNameExist(tournamentName);
        return updateTournamentHours(tournament, newStartHour, newEndHour);
    }

    private Tournament updateTournamentTeamCount(Tournament tournament, Integer newTeamCount) {
        String locationName = tournament.getLocation().getLocationName();
        Date startAt = tournament.getStartAt();
        Date endAt = tournament.getEndAt();
        Integer startHour = tournament.getStartHour();
        Integer endHour = tournament.getEndHour();
        Integer matchDuration = tournament.getMatchDuration();

        validateTeamCount(newTeamCount);

        validateVenuesAvailability(locationName, startAt, endAt, startHour,
            endHour, newTeamCount, matchDuration);

        tournament.setTeamCount(newTeamCount);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentTeamCountById(Long id, Integer newTeamCount) {
        Tournament tournament = validateTournamentIdExist(id);
        return updateTournamentTeamCount(tournament, newTeamCount);
    }

    public Tournament updateTournamentTeamCountByTournamentName(String tournamentName, Integer newTeamCount) {
        Tournament tournament = validateTournamentNameExist(tournamentName);
        return updateTournamentTeamCount(tournament, newTeamCount);
    }
}
