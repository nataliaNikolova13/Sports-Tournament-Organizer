package com.fmi.sporttournament.tournament.dto.request;

import com.fmi.sporttournament.location.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TournamentCreationRequest {
    private String tournamentName;
    private String sportType;
    private Location location;
    private Date startAt;
    private Date endAt;
}