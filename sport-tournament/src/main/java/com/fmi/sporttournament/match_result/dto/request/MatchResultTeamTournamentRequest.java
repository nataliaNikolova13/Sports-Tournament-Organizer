package com.fmi.sporttournament.match_result.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchResultTeamTournamentRequest {
    private String tournamentName;
    private String teamName;
}
