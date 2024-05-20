package com.fmi.sporttournament.Dto.responses.tournament;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
@Data
@RequiredArgsConstructor
public class TournamentResponse {
    private final String tournamentName;
    private final String sportType;
    private final Long locationId;
    private final Date startAt;
    private final Date endAt;
}
