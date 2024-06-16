package com.fmi.sporttournament.match.dto.request;

import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.venue.entity.Venue;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class MatchCreationRequest {
    private Round round;
    private Team team1;
    private Team team2;
    private LocalDateTime matchTime;
    private Venue venue;
}
