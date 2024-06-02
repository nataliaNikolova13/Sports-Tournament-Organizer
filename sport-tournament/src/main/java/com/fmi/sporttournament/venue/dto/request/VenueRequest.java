package com.fmi.sporttournament.venue.dto.request;

import com.fmi.sporttournament.location.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VenueRequest {
    private  Location location;
    private  Long number;
}
