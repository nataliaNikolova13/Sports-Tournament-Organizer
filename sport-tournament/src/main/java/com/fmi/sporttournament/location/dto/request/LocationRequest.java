package com.fmi.sporttournament.location.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationRequest {
    private String locationName;
    private Long venueCount;
}
