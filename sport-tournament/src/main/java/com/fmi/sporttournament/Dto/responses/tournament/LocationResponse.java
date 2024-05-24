package com.fmi.sporttournament.Dto.responses.tournament;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResponse {
    private String locationName;
    private Long venueCount;
}
