package com.fmi.sporttournament.Dto.requests;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ParticipantRequest {
    private final Long userId;
    private final Long teamId;
}
