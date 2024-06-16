package com.fmi.sporttournament.match.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchTeamTournamentRequest {
    private String tournamentName;
    private String teamName;
}
