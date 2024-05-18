package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.LocationDto;
import com.fmi.sporttournament.Dto.TournamentDto;
import com.fmi.sporttournament.entity.Location;
import com.fmi.sporttournament.entity.Tournament;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDto locationToDto(Location location);

    Location locationFromDto(LocationDto locationDto);

    List<LocationDto> locationsToLocationDtos(List<Location> locations);
}
