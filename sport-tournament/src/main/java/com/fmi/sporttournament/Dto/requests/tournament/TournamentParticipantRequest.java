package com.fmi.sporttournament.Dto.requests.tournament;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TournamentParticipantRequest {
    private final String tournamentName;
    private final String teamName;
}
