package com.fmi.sporttournament.location.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResponse {
    private String locationName;
    private Long venueCount;
}
