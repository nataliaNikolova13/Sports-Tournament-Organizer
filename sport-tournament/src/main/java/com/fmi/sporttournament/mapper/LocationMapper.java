package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.LocationDto;

import com.fmi.sporttournament.entity.Location;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDto locationToDto(Location location);

    Location locationFromDto(LocationDto locationDto);

    List<LocationDto> locationsToLocationDtos(List<Location> locations);
}
