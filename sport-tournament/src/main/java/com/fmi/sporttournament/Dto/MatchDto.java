package com.fmi.sporttournament.Dto;

import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.Venue;
import com.fmi.sporttournament.entity.enums.MatchResult;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class MatchDto {
    private Tournament tournament;
    private Venue venue;
    private Team team_1;
    private Team team_2;
    private Date startAt;
    private Date endAt;
    private MatchResult result;
}
