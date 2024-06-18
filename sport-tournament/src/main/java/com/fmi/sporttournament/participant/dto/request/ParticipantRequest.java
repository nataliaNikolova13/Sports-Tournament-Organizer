package com.fmi.sporttournament.participant.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipantRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long teamId;
}
