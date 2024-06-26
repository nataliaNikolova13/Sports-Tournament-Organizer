package com.fmi.sporttournament.tournament.service;

import com.fmi.sporttournament.email.dto.request.EmailRequestAllUsers;
import com.fmi.sporttournament.email.emails.match.MatchInformation;
import com.fmi.sporttournament.email.emails.tournament.TournamentCancellationEmail;

import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.match.service.MatchService;

import com.fmi.sporttournament.round.dto.request.RoundRequest;
import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.round.service.RoundService;

import com.fmi.sporttournament.team.entity.Team;

import com.fmi.sporttournament.tournament.dto.request.TournamentCreationRequest;
import com.fmi.sporttournament.tournament.dto.request.TournamentRegistrationRequest;
import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.tournament.entity.category.TournamentCategory;
import com.fmi.sporttournament.tournament.mapper.TournamentMapper;
import com.fmi.sporttournament.tournament.repository.TournamentRepository;

import com.fmi.sporttournament.location.entity.Location;
import com.fmi.sporttournament.location.service.LocationValidationService;

import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;
import com.fmi.sporttournament.tournament_participant.repository.TournamentParticipantRepository;

import com.fmi.sporttournament.tournament_participant.service.TournamentParticipantValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;
    private final TournamentValidationService tournamentValidationService;

    private final TournamentParticipantRepository tournamentParticipantRepository;

    private final MatchService matchService;

    private final RoundService roundService;

    private final MatchInformation matchInformation;

    private final TournamentParticipantValidationService tournamentParticipantValidationService;

    private final LocationValidationService locationValidationService;

    private final TournamentCancellationEmail tournamentCancellationEmail;

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public List<Tournament> getAllValidTournaments() {
        return tournamentRepository.findValidTournaments();
    }

    public Tournament getTournamentById(Long id) {
        return tournamentValidationService.validateTournamentIdExist(id);
    }

    public Tournament getTournamentByTournamentName(String tournamentName) {
        return tournamentValidationService.validateTournamentNameExist(tournamentName);
    }

    public List<Tournament> getTournamentBySportType(String sportType) {
        return tournamentRepository.findValidTournamentsBySportType(sportType);
    }

    public List<Tournament> getTournamentByLocationName(String locationName) {
        Location location = locationValidationService.validateLocationNameExist(locationName);
        return tournamentRepository.findValidTournamentsByLocation(location);
    }

    public Tournament createTournament(TournamentRegistrationRequest tournamentRegistrationRequest) {
        String tournamentName = tournamentRegistrationRequest.getTournamentName();

        tournamentValidationService.validateTournamentNameNotExist(tournamentName);

        String locationName = tournamentRegistrationRequest.getLocationName();
        Location location = locationValidationService.validateLocationNameExist(locationName);

        String sportType = tournamentRegistrationRequest.getSportType();

        Date startAt = tournamentRegistrationRequest.getStartAt();
        Date endAt = tournamentRegistrationRequest.getEndAt();
        tournamentValidationService.validateRequestDates(startAt, endAt);
        tournamentValidationService.validateLocationAvailabilityDates(location, startAt, endAt);

        Integer startHour = tournamentRegistrationRequest.getStartHour();
        Integer endHour = tournamentRegistrationRequest.getEndHour();
        tournamentValidationService.validateHours(startHour, endHour);

        Integer teamCount = tournamentRegistrationRequest.getTeamCount();
        tournamentValidationService.validateTeamCount(teamCount);

        Integer matchDuration = tournamentRegistrationRequest.getMatchDuration();
        tournamentValidationService.validateMatchDuration(startHour, endHour, matchDuration);

        locationValidationService.validateVenuesAvailability(locationName, startAt, endAt, startHour,
            endHour, teamCount, matchDuration);

        Integer teamMemberCount = tournamentRegistrationRequest.getTeamMemberCount();

        TournamentCreationRequest tournamentCreationRequest =
            new TournamentCreationRequest(tournamentName, sportType,
                tournamentRegistrationRequest.getTournamentCategory(), location,
                startAt, endAt, startHour, endHour,
                teamCount, matchDuration, teamMemberCount);

        Tournament tournament = tournamentMapper.requestToTournament(tournamentCreationRequest);
        return tournamentRepository.save(tournament);
    }

    private void sendTournamentCancellationEmailToParticipants(Tournament tournament) {
        List<Team> teams = tournamentParticipantRepository.findAllEnrolledTeamsInTournament(tournament);
        for (Team team : teams) {
            tournamentCancellationEmail.sendTournamentCancellationEmail(team, tournament.getTournamentName());
        }
    }

    @Transactional
    public void deleteTournamentById(Long id) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        tournamentValidationService.validateDateOfUpdate(tournament.getStartAt());
        sendTournamentCancellationEmailToParticipants(tournament);
        tournamentRepository.deleteById(id);
    }

    @Transactional
    public void deleteTournamentByTournamentName(String tournamentName) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        tournamentValidationService.validateDateOfUpdate(tournament.getStartAt());
        sendTournamentCancellationEmailToParticipants(tournament);
        tournamentRepository.deleteById(tournament.getId());
    }

    private void setTournament(Tournament tournament, String tournamentName,
                               String sportType, TournamentCategory tournamentCategory, Location location,
                               Date startAt, Date endAt,
                               Integer startHour, Integer endHour, Integer teamCount, Integer matchDuration,
                               Integer teamMemberCount) {
        tournament.setTournamentName(tournamentName);
        tournament.setSportType(sportType);
        tournament.setTournamentCategory(tournamentCategory);
        tournament.setLocation(location);
        tournament.setStartAt(startAt);
        tournament.setEndAt(endAt);
        tournament.setStartHour(startHour);
        tournament.setEndHour(endHour);
        tournament.setTeamCount(teamCount);
        tournament.setMatchDuration(matchDuration);
        tournament.setTeamMemberCount(teamMemberCount);
    }

    public Tournament updateTournament(Long id, TournamentRegistrationRequest tournamentRegistrationRequest) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        tournamentParticipantValidationService.validateNoTeamJoinedInTournament(tournament);

        String tournamentName = tournamentRegistrationRequest.getTournamentName();

        if (!tournament.getTournamentName().equals(tournamentName)) {
            tournamentValidationService.validateTournamentNameNotExist(tournamentName);
        }

        String locationName = tournamentRegistrationRequest.getLocationName();
        Location location = locationValidationService.validateLocationNameExist(locationName);

        String sportType = tournamentRegistrationRequest.getSportType();

        Date startAt = tournamentRegistrationRequest.getStartAt();
        Date endAt = tournamentRegistrationRequest.getEndAt();
        tournamentValidationService.validateRequestDates(startAt, endAt);
        tournamentValidationService.validateDateOfUpdate(startAt);

        tournamentValidationService.validateLocationAvailabilityDates(tournament, location, startAt, endAt);

        Integer startHour = tournamentRegistrationRequest.getStartHour();
        Integer endHour = tournamentRegistrationRequest.getEndHour();
        tournamentValidationService.validateHours(startHour, endHour);

        Integer teamCount = tournamentRegistrationRequest.getTeamCount();
        tournamentValidationService.validateTeamCount(teamCount);

        Integer matchDuration = tournamentRegistrationRequest.getMatchDuration();
        tournamentValidationService.validateMatchDuration(startHour, endHour, matchDuration);

        locationValidationService.validateVenuesAvailability(locationName, startAt, endAt, startHour,
            endHour, teamCount, matchDuration);

        Integer teamMemberCount = tournamentRegistrationRequest.getTeamMemberCount();
        TournamentCategory tournamentCategory = tournamentRegistrationRequest.getTournamentCategory();
        setTournament(tournament, tournamentName, sportType, tournamentCategory, location, startAt, endAt, startHour,
            endHour, teamCount,
            matchDuration, teamMemberCount);

        return tournamentRepository.save(tournament);
    }

    private Tournament updateTournamentName(Tournament tournament, String newTournamentName) {
        tournamentParticipantValidationService.validateNoTeamJoinedInTournament(tournament);
        tournamentValidationService.validateDateOfUpdate(tournament.getStartAt());

        if (!tournament.getTournamentName().equals(newTournamentName)) {
            tournamentValidationService.validateTournamentNameNotExist(newTournamentName);
        }

        tournament.setTournamentName(newTournamentName);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentNameById(Long id, String newTournamentName) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        return updateTournamentName(tournament, newTournamentName);
    }

    public Tournament updateTournamentNameByTournamentName(String currentTournamentName, String newTournamentName) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(currentTournamentName);
        return updateTournamentName(tournament, newTournamentName);
    }

    private Tournament updateTournamentLocation(Tournament tournament, String newLocationName) {
        tournamentParticipantValidationService.validateNoTeamJoinedInTournament(tournament);
        Location location = locationValidationService.validateLocationNameExist(newLocationName);

        Date startAt = tournament.getStartAt();
        Date endAt = tournament.getEndAt();

        tournamentValidationService.validateRequestDates(tournament.getStartAt(), tournament.getEndAt());

        tournamentValidationService.validateLocationAvailabilityDates(tournament, location, startAt, endAt);
        tournamentValidationService.validateDateOfUpdate(startAt);

        Integer startHour = tournament.getStartHour();
        Integer endHour = tournament.getEndHour();
        Integer teamCount = tournament.getTeamCount();
        Integer matchDuration = tournament.getMatchDuration();

        locationValidationService.validateVenuesAvailability(location.getLocationName(), startAt, endAt, startHour,
            endHour, teamCount, matchDuration);

        tournament.setLocation(location);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentLocationById(Long id, String newLocationName) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        return updateTournamentLocation(tournament, newLocationName);
    }

    public Tournament updateTournamentLocationByTournamentName(String tournamentName, String newLocationName) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        return updateTournamentLocation(tournament, newLocationName);
    }

    private Tournament updateTournamentSportType(Tournament tournament, String newSportType) {
        tournamentParticipantValidationService.validateNoTeamJoinedInTournament(tournament);
        tournamentValidationService.validateDateOfUpdate(tournament.getStartAt());
        tournament.setSportType(newSportType);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentSportTypeById(Long id, String newSportType) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        return updateTournamentSportType(tournament, newSportType);
    }

    public Tournament updateTournamentSportTypeByTournamentName(String tournamentName, String newSportType) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        return updateTournamentSportType(tournament, newSportType);
    }

    private Tournament updateTournamentCategory(Tournament tournament, TournamentCategory tournamentCategory) {
        tournamentParticipantValidationService.validateNoTeamJoinedInTournament(tournament);
        tournament.setTournamentCategory(tournamentCategory);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentCategoryById(Long id, TournamentCategory tournamentCategory) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        return updateTournamentCategory(tournament, tournamentCategory);
    }

    public Tournament updateTournamentCategoryByTournamentName(String tournamentName,
                                                               TournamentCategory tournamentCategory) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        return updateTournamentCategory(tournament, tournamentCategory);
    }

    private Tournament updateTournamentDates(Tournament tournament, Date newStartAt, Date newEndAt) {
        tournamentParticipantValidationService.validateNoTeamJoinedInTournament(tournament);
        tournamentValidationService.validateDateOfUpdate(tournament.getStartAt());
        tournamentValidationService.validateRequestDates(newStartAt, newEndAt);

        Location location = tournament.getLocation();
        tournamentValidationService.validateLocationAvailabilityDates(tournament, location, newStartAt, newEndAt);

        Integer startHour = tournament.getStartHour();
        Integer endHour = tournament.getEndHour();
        Integer teamCount = tournament.getTeamCount();
        Integer matchDuration = tournament.getMatchDuration();

        locationValidationService.validateVenuesAvailability(location.getLocationName(), newStartAt, newEndAt,
            startHour,
            endHour, teamCount, matchDuration);

        tournament.setStartAt(newStartAt);
        tournament.setEndAt(newEndAt);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentDatesById(Long id, Date newStartAt, Date newEndAt) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        return updateTournamentDates(tournament, newStartAt, newEndAt);
    }

    public Tournament updateTournamentDatesByTournamentName(String tournamentName, Date newStartAt, Date newEndAt) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        return updateTournamentDates(tournament, newStartAt, newEndAt);
    }

    private Tournament updateTournamentMatchDuration(Tournament tournament, Integer newMatchDuration) {
        tournamentParticipantValidationService.validateNoTeamJoinedInTournament(tournament);
        String locationName = tournament.getLocation().getLocationName();

        Date startAt = tournament.getStartAt();
        Date endAt = tournament.getEndAt();
        tournamentValidationService.validateDateOfUpdate(startAt);

        Integer startHour = tournament.getStartHour();
        Integer endHour = tournament.getEndHour();
        Integer teamCount = tournament.getTeamCount();
        tournamentValidationService.validateMatchDuration(startHour, endHour, newMatchDuration);

        locationValidationService.validateVenuesAvailability(locationName, startAt, endAt, startHour,
            endHour, teamCount, newMatchDuration);

        tournament.setMatchDuration(newMatchDuration);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentMatchDurationById(Long id, Integer newMatchDuration) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        return updateTournamentMatchDuration(tournament, newMatchDuration);
    }

    public Tournament updateTournamentMatchDurationByTournamentName(String tournamentName, Integer newMatchDuration) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        return updateTournamentMatchDuration(tournament, newMatchDuration);
    }

    private Tournament updateTournamentHours(Tournament tournament, Integer newStartHour, Integer newEndHour) {
        tournamentParticipantValidationService.validateNoTeamJoinedInTournament(tournament);
        String locationName = tournament.getLocation().getLocationName();

        Date startAt = tournament.getStartAt();
        Date endAt = tournament.getEndAt();
        tournamentValidationService.validateDateOfUpdate(startAt);

        tournamentValidationService.validateHours(newStartHour, newEndHour);

        Integer teamCount = tournament.getTeamCount();

        Integer matchDuration = tournament.getMatchDuration();
        tournamentValidationService.validateMatchDuration(newStartHour, newEndHour, matchDuration);

        locationValidationService.validateVenuesAvailability(locationName, startAt, endAt, newStartHour,
            newEndHour, teamCount, matchDuration);

        tournament.setStartHour(newStartHour);
        tournament.setEndHour(newEndHour);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentHoursById(Long id, Integer newStartHour, Integer newEndHour) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        return updateTournamentHours(tournament, newStartHour, newEndHour);
    }

    public Tournament updateTournamentHoursByTournamentName(String tournamentName, Integer newStartHour,
                                                            Integer newEndHour) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        return updateTournamentHours(tournament, newStartHour, newEndHour);
    }

    private Tournament updateTournamentTeamCount(Tournament tournament, Integer newTeamCount) {
        tournamentParticipantValidationService.validateNoTeamJoinedInTournament(tournament);
        String locationName = tournament.getLocation().getLocationName();

        Date startAt = tournament.getStartAt();
        Date endAt = tournament.getEndAt();
        tournamentValidationService.validateDateOfUpdate(startAt);

        Integer startHour = tournament.getStartHour();
        Integer endHour = tournament.getEndHour();
        Integer matchDuration = tournament.getMatchDuration();

        tournamentValidationService.validateTeamCount(newTeamCount);

        locationValidationService.validateVenuesAvailability(locationName, startAt, endAt, startHour,
            endHour, newTeamCount, matchDuration);

        tournament.setTeamCount(newTeamCount);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentTeamCountById(Long id, Integer newTeamCount) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        return updateTournamentTeamCount(tournament, newTeamCount);
    }

    public Tournament updateTournamentTeamCountByTournamentName(String tournamentName, Integer newTeamCount) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        return updateTournamentTeamCount(tournament, newTeamCount);
    }

    private Tournament updateTournamentTeamMemberCount(Tournament tournament, Integer newTeamMemberCount) {
        tournamentParticipantValidationService.validateNoTeamJoinedInTournament(tournament);
        tournament.setTeamCount(newTeamMemberCount);
        return tournamentRepository.save(tournament);
    }

    public Tournament updateTournamentTeamMemberCountById(Long id, Integer newTeamCount) {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        return updateTournamentTeamMemberCount(tournament, newTeamCount);
    }

    public Tournament updateTournamentTeamMemberCountByTournamentName(String tournamentName, Integer newTeamCount) {
        Tournament tournament = tournamentValidationService.validateTournamentNameExist(tournamentName);
        return updateTournamentTeamMemberCount(tournament, newTeamCount);
    }

    private void scheduleTournamentMatches(Tournament tournament) {
        List<Team> teams = tournamentParticipantRepository.findAllTeamsByTournamentStatusAndTournament(
            TournamentParticipantStatus.joined, tournament);
        int roundCount = (int) (Math.log(tournament.getTeamCount()) / Math.log(2));
        Integer currentRoundNumber = 1;
        Date rounsStartDate = tournament.getStartAt();
        while (currentRoundNumber <= roundCount) {
            RoundRequest roundRequest = new RoundRequest(tournament, currentRoundNumber);
            Round round = roundService.createRound(roundRequest);
            Pair<List<Match>, Date> matches =
                matchService.scheduleMatchesInRounds(round, teams, rounsStartDate);
            teams = matchService.determineWinners(matches.getFirst());
            rounsStartDate = matches.getSecond();
            currentRoundNumber++;
        }
    }

    @Transactional
    public Tournament startTournamentById(Long id) throws IOException {
        Tournament tournament = tournamentValidationService.validateTournamentIdExist(id);
        tournamentParticipantValidationService.validateTournamentCapacityBeforeStart(tournament);
        scheduleTournamentMatches(tournament);
        List<Team> teams = tournamentParticipantRepository.findAllTeamsByTournamentStatusAndTournament(
            TournamentParticipantStatus.joined, tournament);
        for (Team team : teams) {
            EmailRequestAllUsers emailRequestAllUsers =
                new EmailRequestAllUsers("Results", "Tournament Results of all teams");
            matchInformation.sendEmailWithCsvAttachment(team, emailRequestAllUsers, "match_results.csv", tournament);
        }
        return tournament;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void startTournaments() throws IOException {
        ZonedDateTime startOfToday = LocalDate.now(ZoneId.of("UTC")).atStartOfDay(ZoneId.of("UTC"));
        ZonedDateTime endOfToday = startOfToday.plusDays(1);

        Date startOfTodayDate = Date.from(startOfToday.toInstant());
        Date endOfTodayDate = Date.from(endOfToday.toInstant());

        List<Tournament> tournaments = tournamentRepository.findTournamentsByStartAtBetween(startOfTodayDate, endOfTodayDate);

        for (Tournament tournament : tournaments) {
            startTournamentById(tournament.getId());
        }
    }
}
