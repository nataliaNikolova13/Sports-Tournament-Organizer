package com.fmi.sporttournament.match.dto.response;

import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.venue.entity.Venue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class MatchResponse {
    private Integer roundNumber;
    private String teamName1;
    private String teamName2;
    private LocalDateTime matchTime;
    private String locationName;
    private Integer venueNumber;
}
