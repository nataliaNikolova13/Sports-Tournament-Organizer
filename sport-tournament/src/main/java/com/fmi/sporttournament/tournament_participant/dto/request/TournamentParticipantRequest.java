package com.fmi.sporttournament.tournament_participant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TournamentParticipantRequest {
    private String tournamentName;
    private String teamName;
}
