package com.fmi.sporttournament.match_result.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchResultTeamTournamentRequest {
    @NotNull
    @NotBlank
    private String tournamentName;

    @NotNull
    @NotBlank
    private String teamName;
}
