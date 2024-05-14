package com.fmi.sporttournament.Dto.requests;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ParticipantRequest {
    private Long userId;
    private Long teamId;
}
