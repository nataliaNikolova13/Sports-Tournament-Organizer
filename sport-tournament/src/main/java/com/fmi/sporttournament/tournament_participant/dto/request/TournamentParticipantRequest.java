package com.fmi.sporttournament.tournament_participant.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TournamentParticipantRequest {
    @NotNull
    @NotBlank
    private String tournamentName;

    @NotNull
    @NotBlank
    private String teamName;
}
