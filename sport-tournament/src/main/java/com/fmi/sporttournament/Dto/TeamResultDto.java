package com.fmi.sporttournament.Dto;

import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.entity.Tournament;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TeamResultDto {
    private Tournament tournament;
    private Team team;
    private Long score;
}
