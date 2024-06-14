package com.fmi.sporttournament.location.service;

import com.fmi.sporttournament.location.dto.request.LocationRequest;
import com.fmi.sporttournament.tournament.repository.TournamentRepository;
import com.fmi.sporttournament.venue.dto.request.VenueRequest;

import com.fmi.sporttournament.location.entity.Location;

import com.fmi.sporttournament.location.mapper.LocationMapper;

import com.fmi.sporttournament.location.repository.LocationRepository;

import com.fmi.sporttournament.venue.service.VenueService;
import jakarta.transaction.Transactional;
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
    private final TournamentRepository tournamentRepository;

    private void validateLocationNameNotExist(String locationName) {
        if (getLocationByName(locationName).isPresent()) {
            throw new IllegalArgumentException("Location with the name " + locationName + " already exists");
        }
    }

    private Location validateLocationIdExist(Long id) {
        Optional<Location> location = getLocationById(id);
        if (location.isEmpty()) {
            throw new IllegalArgumentException("Location with this id " + id + " doesn't exist");
        }
        return location.get();
    }

    private Location validateLocationNameExist(String locationName) {
        Optional<Location> location = getLocationByName(locationName);
        if (location.isEmpty()) {
            throw new IllegalArgumentException("Location with the name " + locationName + " doesn't exist");
        }
        return location.get();
    }

    private void validateVenueCount(Long venueCount) {
        if (venueCount <= 0) {
            throw new IllegalArgumentException("The count of the venues can't be negative or zero");
        }
    }

    private void validateLocationNameIsNotBlank(String locationName) {
        if (locationName.isBlank()) {
            throw new IllegalArgumentException("Location name can't be blank");
        }
    }

    private void validateLocationIsNotUsedInTournament(Location location) {
        if (!tournamentRepository.findValidTournamentsByLocation(location).isEmpty()) {
            throw new IllegalStateException("The location is used for a tournament and its venues can't be changed");
        }
    }

    public Long countVenuesByLocationId(Long locationId) {
        return locationRepository.countVenuesByLocationId(locationId);
    }

    public Long countVenuesByLocationName(String locationName) {
        return locationRepository.countVenuesByLocationName(locationName);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public Optional<Location> getLocationByName(String locationName) {
        return locationRepository.findByLocationName(locationName);
    }

    @Transactional
    public Location createLocation(LocationRequest locationRequest) {
        String locationName = locationRequest.getLocationName();
        validateLocationNameIsNotBlank(locationName);
        validateLocationNameNotExist(locationName);
        Long venueCount = locationRequest.getVenueCount();
        validateVenueCount(venueCount);

        Location location = locationMapper.requestToLocation(locationRequest);
        location = locationRepository.save(location);

        for (int i = 0; i < venueCount; i++) {
            VenueRequest venueRequest = new VenueRequest(location, (long) i + 1);
            venueService.createVenue(venueRequest);
        }

        return location;
    }

    public void deleteLocation(Long id) {
        Location location = validateLocationIdExist(id);
        validateLocationIsNotUsedInTournament(location);
        locationRepository.deleteById(id);
    }

    public void deleteByLocationName(String locationName) {
        Location location = validateLocationNameExist(locationName);
        validateLocationIsNotUsedInTournament(location);
        locationRepository.deleteById(location.getId());
    }

    @Transactional
    public Location updateLocation(Long id, LocationRequest locationRequest) {
        Location location = validateLocationIdExist(id);

        String locationName = locationRequest.getLocationName();
        validateLocationNameIsNotBlank(locationName);

        if (!location.getLocationName().equals(locationName)) {
            validateLocationNameNotExist(locationName);
        }
        location.setLocationName(locationName);

        locationRepository.save(location);

        return updateVenueCount(location, locationRequest.getVenueCount());
    }

    private Location updateLocationName(Location location, String newLocationName) {
        validateLocationNameIsNotBlank(newLocationName);

        if (!location.getLocationName().equals(newLocationName)) {
            validateLocationNameNotExist(newLocationName);
        }

        location.setLocationName(newLocationName);
        return locationRepository.save(location);
    }

    public Location updateLocationNameById(Long id, String newLocationName) {
        Location location = validateLocationIdExist(id);
        return updateLocationName(location, newLocationName);
    }

    public Location updateLocationNameByLocationName(String currentLocationName, String newLocationName) {
        Location location = validateLocationNameExist(currentLocationName);
       return updateLocationName(location, newLocationName);
    }

    private Location updateVenueCount(Location location, Long newVenueCount) {
        validateLocationIsNotUsedInTournament(location);
        Long currentVenueCount = countVenuesByLocationId(location.getId());

        if (newVenueCount > currentVenueCount) {
            for (long i = currentVenueCount + 1; i <= newVenueCount; i++) {
                VenueRequest venueRequest = new VenueRequest(location, i);
                venueService.createVenue(venueRequest);
            }
        } else if (newVenueCount < currentVenueCount) {
            venueService.deleteExcessVenues(location.getLocationName(), currentVenueCount, newVenueCount);
        }
        return locationRepository.save(location);
    }

    public Location updateVenueCountById(Long id, Long newVenueCount) {
        Location location = validateLocationIdExist(id);
        return updateVenueCount(location, newVenueCount);
    }


    public Location updateVenueCountByLocationName(String locationName, Long newVenueCount) {
        Location location = validateLocationNameExist(locationName);
        return updateVenueCount(location, newVenueCount);
    }
}
