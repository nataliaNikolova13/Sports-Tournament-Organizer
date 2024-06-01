package com.fmi.sporttournament.Dto.requests.tournament;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationRequest {
    private String locationName;
    private Long venueCount;
}
