package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.requests.tournament.VenueRequest;
import com.fmi.sporttournament.Dto.responses.tournament.VenueResponse;

import com.fmi.sporttournament.entity.Venue;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VenueMapper {
    @Mapping(target = "id", ignore = true)
    Venue requestToVenue(VenueRequest venueRequest);

    @Mapping(source = "location.id", target = "locationId")
    VenueResponse venueToResponse(Venue venue);
}
