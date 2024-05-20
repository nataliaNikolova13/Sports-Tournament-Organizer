package com.fmi.sporttournament.services;

import com.fmi.sporttournament.Dto.requests.tournament.LocationRequest;
import com.fmi.sporttournament.Dto.requests.tournament.VenueRequest;
import com.fmi.sporttournament.entity.Location;
import com.fmi.sporttournament.entity.Venue;

import com.fmi.sporttournament.mapper.LocationMapper;
import com.fmi.sporttournament.repositories.LocationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final VenueService venueService;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public Optional<Location> getLocationByName(String locationName) {
        return locationRepository.findByLocationName(locationName);
    }

    public void removeLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public Location createLocation(LocationRequest locationRequest) {
        String locationName = locationRequest.getLocationName();

        if(getLocationByName(locationName).isPresent()){
            throw new IllegalArgumentException("Location with this name already exists");
        }

        Location location = locationMapper.requestToLocation(locationRequest);
        location = locationRepository.save(location);
        Long venueCount = locationRequest.getVenueCount();

        for (int i = 0; i < venueCount; i++) {
            VenueRequest venueRequest = new VenueRequest(location, (long) i + 1);
            venueService.createVenue(venueRequest);
        }

        return location;
    }

    public void removeByLocationName(String locationName) {
        Optional<Location> location = locationRepository.findByLocationName(locationName);
        location.ifPresent(value -> removeLocation(value.getId()));
    }
}
