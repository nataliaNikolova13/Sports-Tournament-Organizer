package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.requests.tournament.LocationRequest;
import com.fmi.sporttournament.Dto.responses.tournament.LocationResponse;
import com.fmi.sporttournament.entity.Location;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationResponse locationToResponse(Location location);

    @Mapping(target = "id", ignore = true)
    Location requestToLocation(LocationRequest locationRequest);

}
