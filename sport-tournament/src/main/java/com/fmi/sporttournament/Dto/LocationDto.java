package com.fmi.sporttournament.Dto;

import com.fmi.sporttournament.entity.Location;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.Venue;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class LocationDto {

    private Location locationName;

    private Set<Venue> venues = new HashSet<>();

    private Set<Tournament> tournaments = new HashSet<>();
}
