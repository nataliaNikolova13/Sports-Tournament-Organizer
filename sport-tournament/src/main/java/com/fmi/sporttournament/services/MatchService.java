package com.fmi.sporttournament.services;

import com.fmi.sporttournament.entity.*;
import com.fmi.sporttournament.repositories.MatchRepository;
import com.fmi.sporttournament.repositories.RoundRepository;
import com.fmi.sporttournament.repositories.TeamRepository;
import com.fmi.sporttournament.repositories.VenueRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@Data
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final RoundService roundService;
    private final VenueRepository venueRepository;

    public void createMatches(Tournament tournament, List<Team> teams, int roundNumber) {
        Round round = roundService.createRound(tournament, roundNumber);

        List<Venue> venues = venueRepository.findAll();
        Iterator<Venue> venueIterator = venues.iterator();

        LocalDateTime matchTime = LocalDateTime
                .ofInstant(tournament.getStartAt().toInstant(), ZoneId.systemDefault());

        for (int i = 0; i < teams.size(); i += 2) {
            if (i + 1 < teams.size()) {
                if (!venueIterator.hasNext()) {
                    venueIterator = venues.iterator(); // Restart venue iterator if we run out of venues
                }
                Venue venue = venueIterator.next();
                Match match = createMatch(round, teams.get(i), teams.get(i+1), matchTime, venue);
                matchTime = matchTime.plusHours(1);
            }else{
                // TO DO if there is odd number of teams, one goes to the next round, without a match
            }
        }

    }

    public Match createMatch(Round round, Team team1, Team team2, LocalDateTime time, Venue venue){
        Match match = new Match();
        match.setRound(round);
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setMatchTime(time);
        match.setVenue(venue);
        return matchRepository.save(match);

    }
}
