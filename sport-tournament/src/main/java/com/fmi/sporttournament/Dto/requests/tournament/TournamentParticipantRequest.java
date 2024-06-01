package com.fmi.sporttournament.Dto.requests.tournament;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TournamentParticipantRequest {
    private String tournamentName;
    private String teamName;
}
