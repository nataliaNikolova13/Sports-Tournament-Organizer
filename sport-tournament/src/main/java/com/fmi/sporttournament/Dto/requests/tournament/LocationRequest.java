package com.fmi.sporttournament.Dto.requests.tournament;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LocationRequest {
    private final String locationName;
    private final Long venueCount;
}
