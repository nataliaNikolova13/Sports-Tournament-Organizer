package com.fmi.sporttournament.Dto;

import com.fmi.sporttournament.entity.Location;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.Venue;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class LocationDto {
    private String locationName;
    private Set<Venue> venue;
    private Set<Tournament> tournaments;
}
