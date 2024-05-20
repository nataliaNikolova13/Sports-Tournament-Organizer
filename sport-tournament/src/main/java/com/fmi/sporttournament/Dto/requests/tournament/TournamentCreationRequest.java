package com.fmi.sporttournament.Dto.requests.tournament;

import com.fmi.sporttournament.entity.Location;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class TournamentCreationRequest {
    private final String tournamentName;
    private final String sportType;
    private final Location location;
    private final Date startAt;
    private final Date endAt;
}