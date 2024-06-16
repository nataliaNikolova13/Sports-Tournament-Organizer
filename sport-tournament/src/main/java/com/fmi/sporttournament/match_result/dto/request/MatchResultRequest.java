package com.fmi.sporttournament.match_result.dto.request;

import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.team.entity.Team;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchResultRequest {
    private Match match;
    private Team winningTeam;
    private Integer scoreTeam1;
    private Integer scoreTeam2;
}
