package com.fmi.sporttournament.tournament.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TournamentRegistrationRequest {
    private String tournamentName;
    private String sportType;
    private String locationName;
    private Date startAt;
    private Date endAt;
}