package com.fmi.sporttournament.location.service;

import com.fmi.sporttournament.location.dto.request.LocationRequest;
import com.fmi.sporttournament.location.entity.Location;
import com.fmi.sporttournament.location.mapper.LocationMapper;
import com.fmi.sporttournament.location.repository.LocationRepository;

import com.fmi.sporttournament.tournament.service.TournamentValidationService;

import com.fmi.sporttournament.venue.dto.request.VenueRequest;

import com.fmi.sporttournament.venue.service.VenueService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final LocationValidationService locationValidationService;

    private final VenueService venueService;

    private final TournamentValidationService tournamentValidationService;

    public Long countVenuesByLocationId(Long locationId) {
        return locationRepository.countVenuesByLocationId(locationId);
    }

    public Long countVenuesByLocationName(String locationName) {
        return locationRepository.countVenuesByLocationName(locationName);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        return locationValidationService.validateLocationIdExist(id);
    }

    public Location getLocationByName(String locationName) {
        return locationValidationService.validateLocationNameExist(locationName);
    }

    @Transactional
    public Location createLocation(LocationRequest locationRequest) {
        String locationName = locationRequest.getLocationName();

        locationValidationService.validateLocationNameNotExist(locationName);
        Long venueCount = locationRequest.getVenueCount();

        Location location = locationMapper.requestToLocation(locationRequest);
        location = locationRepository.save(location);

        for (int i = 0; i < venueCount; i++) {
            VenueRequest venueRequest = new VenueRequest(location, (long) i + 1);
            venueService.createVenue(venueRequest);
        }

        return location;
    }

    public void deleteLocation(Long id) {
        Location location = locationValidationService.validateLocationIdExist(id);
        tournamentValidationService.validateLocationIsNotUsedInTournament(location);
        locationRepository.deleteById(id);
    }

    public void deleteByLocationName(String locationName) {
        Location location = locationValidationService.validateLocationNameExist(locationName);
        tournamentValidationService.validateLocationIsNotUsedInTournament(location);
        locationRepository.deleteById(location.getId());
    }

    @Transactional
    public Location updateLocation(Long id, LocationRequest locationRequest) {
        Location location = locationValidationService.validateLocationIdExist(id);

        String locationName = locationRequest.getLocationName();

        tournamentValidationService.validateLocationIsNotUsedInTournament(location);

        if (!location.getLocationName().equals(locationName)) {
            locationValidationService.validateLocationNameNotExist(locationName);
        }
        location.setLocationName(locationName);

        locationRepository.save(location);

        return updateVenueCount(location, locationRequest.getVenueCount());
    }

    private Location updateLocationName(Location location, String newLocationName) {
        if (!location.getLocationName().equals(newLocationName)) {
            locationValidationService.validateLocationNameNotExist(newLocationName);
        }
        tournamentValidationService.validateLocationIsNotUsedInTournament(location);

        location.setLocationName(newLocationName);
        return locationRepository.save(location);
    }

    public Location updateLocationNameById(Long id, String newLocationName) {
        Location location = locationValidationService.validateLocationIdExist(id);
        return updateLocationName(location, newLocationName);
    }

    public Location updateLocationNameByLocationName(String currentLocationName, String newLocationName) {
        Location location = locationValidationService.validateLocationNameExist(currentLocationName);
        return updateLocationName(location, newLocationName);
    }

    private Location updateVenueCount(Location location, Long newVenueCount) {
        tournamentValidationService.validateLocationIsNotUsedInTournament(location);
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
        Location location = locationValidationService.validateLocationIdExist(id);
        return updateVenueCount(location, newVenueCount);
    }

    public Location updateVenueCountByLocationName(String locationName, Long newVenueCount) {
        Location location = locationValidationService.validateLocationNameExist(locationName);
        return updateVenueCount(location, newVenueCount);
    }
}
