package com.fmi.sporttournament.Dto.requests.tournament;

import com.fmi.sporttournament.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VenueRequest {
    private  Location location;
    private  Long number;
}
