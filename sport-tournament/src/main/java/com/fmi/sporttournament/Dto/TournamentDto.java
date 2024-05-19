package com.fmi.sporttournament.Dto;

import com.fmi.sporttournament.entity.Location;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class TournamentDto {
    private String tournamentName;
    private String sportType;
    private Location location;
    private Date startAt;
    private Date endAt;
}
