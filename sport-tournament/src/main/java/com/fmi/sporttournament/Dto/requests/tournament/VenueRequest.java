package com.fmi.sporttournament.Dto.requests.tournament;

import com.fmi.sporttournament.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class VenueRequest {
    private final Location location;
    private final Long number;
}
