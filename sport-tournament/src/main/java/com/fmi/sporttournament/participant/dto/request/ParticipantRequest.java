package com.fmi.sporttournament.participant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipantRequest {
    private Long userId;
    private Long teamId;
}
