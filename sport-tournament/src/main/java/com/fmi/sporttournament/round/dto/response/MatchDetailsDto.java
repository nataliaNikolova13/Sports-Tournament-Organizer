package com.fmi.sporttournament.round.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchDetailsDto {
    private String team1Name;
    private String team2Name;
    private Integer scoreTeam1;
    private Integer scoreTeam2;
    private String winningTeamName;
}
