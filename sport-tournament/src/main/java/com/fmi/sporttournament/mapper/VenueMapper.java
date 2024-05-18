package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.LocationDto;
import com.fmi.sporttournament.Dto.VenueDto;
import com.fmi.sporttournament.entity.Location;
import com.fmi.sporttournament.entity.Venue;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface VenueMapper {
    VenueDto venueToDto(Venue venue);

    Venue venueFromDto(VenueDto venueDto);

    List<VenueDto> venuesToVenuesDtos(List<Venue> venues);
}
