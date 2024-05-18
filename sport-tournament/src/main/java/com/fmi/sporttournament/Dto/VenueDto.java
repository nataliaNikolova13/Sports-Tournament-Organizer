package com.fmi.sporttournament.Dto;

import com.fmi.sporttournament.entity.Location;

import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
public class VenueDto {
    private Long id;

    private Location location;
}
