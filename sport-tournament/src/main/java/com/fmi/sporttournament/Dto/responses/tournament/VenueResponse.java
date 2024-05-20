package com.fmi.sporttournament.Dto.responses.tournament;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class VenueResponse {
    private final Long locationId;
    private final Long number;
}
