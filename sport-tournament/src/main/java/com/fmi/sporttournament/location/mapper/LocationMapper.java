package com.fmi.sporttournament.location.mapper;

import com.fmi.sporttournament.location.dto.request.LocationRequest;
import com.fmi.sporttournament.location.dto.response.LocationResponse;

import com.fmi.sporttournament.location.entity.Location;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationResponse locationToResponse(Location location, Long venueCount);

    @Mapping(target = "id", ignore = true)
    Location requestToLocation(LocationRequest locationRequest);

}
