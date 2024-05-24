package com.fmi.sporttournament.Dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipantRequest {
    private Long userId;
    private Long teamId;
}
