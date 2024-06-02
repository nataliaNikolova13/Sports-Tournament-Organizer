package com.fmi.sporttournament.venue.mapper;

import com.fmi.sporttournament.venue.dto.request.VenueRequest;
import com.fmi.sporttournament.venue.dto.response.VenueResponse;

import com.fmi.sporttournament.venue.entity.Venue;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VenueMapper {
    @Mapping(target = "id", ignore = true)
    Venue requestToVenue(VenueRequest venueRequest);

    @Mapping(source = "location.id", target = "locationId")
    VenueResponse venueToResponse(Venue venue);
}
