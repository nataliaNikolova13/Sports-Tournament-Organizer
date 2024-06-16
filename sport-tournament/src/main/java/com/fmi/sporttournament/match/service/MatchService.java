package com.fmi.sporttournament.match.service;

import com.fmi.sporttournament.location.entity.Location;
import com.fmi.sporttournament.location.repository.LocationRepository;
import com.fmi.sporttournament.match.dto.request.MatchCreationRequest;
import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.match.mapper.MatchMapper;
import com.fmi.sporttournament.match.repository.MatchRepository;
import com.fmi.sporttournament.match_result.entity.MatchResult;
import com.fmi.sporttournament.match_result.service.MatchResultService;
import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.round.service.RoundService;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.repository.TeamRepository;
import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.tournament.repository.TournamentRepository;
import com.fmi.sporttournament.venue.entity.Venue;
import com.fmi.sporttournament.venue.repository.VenueRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final RoundService roundService;
    private final VenueRepository venueRepository;
    private final MatchResultService matchResultService;
    private final LocationRepository locationRepository;

    private Venue validateVenue(Location location, Long number) {
        Optional<Venue> venue = venueRepository.findByLocationAndNumber(location, number);
        if (venue.isEmpty()) {
            throw new IllegalArgumentException(
                "There isn't such venue at location " + location.getLocationName() + " with number " + number);
        }
        return venue.get();
    }

    private Tournament validTournamentExist(String tournamentName) {
        Optional<Tournament> tournament = tournamentRepository.findByTournamentName(tournamentName);
        if (tournament.isEmpty()) {
            throw new IllegalArgumentException("The tournament doesn't exist");
        }
        return tournament.get();
    }

    private Team validTeamExist(String teamName) {
        Optional<Team> team = teamRepository.findByName(teamName);
        if (team.isEmpty()) {
            throw new IllegalArgumentException("The team doesn't exist");
        }
        return team.get();
    }

    private LocalDateTime combineDateAndHour(Date date, int hour) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate.atTime(hour, 0);
    }

    private Date addOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    public List<Match> getMatchesByTournamentName(String tournamentName) {
        Tournament tournament = validTournamentExist(tournamentName);
        return matchRepository.findAllByTournamentOrderByRoundNumber(tournament);
    }

    public List<Match> getMatchesByTournamentNameAndTeamName(String tournamentName, String teamName) {
        Tournament tournament = validTournamentExist(tournamentName);
        Team team = validTeamExist(teamName);
        return matchRepository.findAllByTournamentAndTeamOrderByRoundNumber(tournament, team);
    }

    public Pair<List<Match>, Date> scheduleMatchesInRounds(Round round, List<Team> teams, Date startDate) {
        Tournament tournament = round.getTournament();
        Location location = tournament.getLocation();
        List<Match> schedule = new ArrayList<>();
        Collections.shuffle(teams);

        List<Team> currentRoundTeams = new ArrayList<>(teams);
        int matchDuration = tournament.getMatchDuration();
        int startHour = tournament.getStartHour();
        int endHour = tournament.getEndHour();
        int teamCount = teams.size();
        Long venueCount = locationRepository.countVenuesByLocationId(location.getId());
        while (teamCount > 0) {
            Integer startHourOfMatch = startHour;
            while ((startHourOfMatch + matchDuration <= endHour) && (teamCount > 0)) {
                Long currentVenueNumber = 1L;
                while ((currentVenueNumber <= venueCount) && (teamCount > 0)) {
                    Venue venue = validateVenue(location, currentVenueNumber);
                    LocalDateTime time = combineDateAndHour(startDate, startHourOfMatch);
                    MatchCreationRequest matchCreationRequest =
                        new MatchCreationRequest(round, currentRoundTeams.get(teamCount - 1),
                            currentRoundTeams.get(teamCount - 2), time, venue);
                    teamCount -= 2;
                    currentVenueNumber += 1;

                    Match match = matchMapper.requestToMatch(matchCreationRequest);
                    matchRepository.save(match);
                    schedule.add(match);
                }
                startHourOfMatch += matchDuration;
            }
            startDate = addOneDay(startDate);
        }
        return Pair.of(schedule, startDate);
    }

    public List<Team> determineWinners(List<Match> matches) {
        List<Team> winners = new ArrayList<>();
        for (Match match : matches) {
            MatchResult matchResult = matchResultService.createMatchResult(match);
            winners.add(matchResult.getWinningTeam());
        }
        return winners;
    }
}
