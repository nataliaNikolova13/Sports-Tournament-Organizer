package com.fmi.sporttournament.match_result.dto.response;

import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchResultResponse {
    private Integer matchId;
    private Integer roundNumber;
    private String teamName1;
    private String teamName2;
    private String winningTeamName;
    private Integer scoreTeam1;
    private Integer scoreTeam2;
}
