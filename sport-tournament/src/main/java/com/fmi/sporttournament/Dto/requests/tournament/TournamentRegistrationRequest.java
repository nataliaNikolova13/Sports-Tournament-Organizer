package com.fmi.sporttournament.Dto.requests.tournament;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class TournamentRegistrationRequest {
    private final String tournamentName;
    private final String sportType;
    private final String locationName;
    private final Date startAt;
    private final Date endAt;
}
