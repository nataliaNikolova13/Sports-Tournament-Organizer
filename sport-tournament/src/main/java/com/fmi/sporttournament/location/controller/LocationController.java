package com.fmi.sporttournament.location.controller;

import com.fmi.sporttournament.location.dto.request.LocationRequest;
import com.fmi.sporttournament.location.dto.response.LocationResponse;
import com.fmi.sporttournament.location.entity.Location;
import com.fmi.sporttournament.location.mapper.LocationMapper;
import com.fmi.sporttournament.location.service.LocationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable Long id) {
        Location location = locationService.getLocationById(id);
        Long venueCount = locationService.countVenuesByLocationId(id);
        return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
    }

    @GetMapping("/name/{locationName}")
    public ResponseEntity<LocationResponse> getLocationByName(@PathVariable String locationName) {
        Location location = locationService.getLocationByName(locationName);
        Long venueCount = locationService.countVenuesByLocationId(location.getId());
        return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
    }

    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(@RequestBody @Valid LocationRequest locationRequest) {
        Location location = locationService.createLocation(locationRequest);
        Long locationId = location.getId();
        Long venueCount = locationService.countVenuesByLocationId(locationId);
        return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocationById(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/name/{locationName}")
    public ResponseEntity<Void> deleteLocationByLocationName(@PathVariable String locationName) {
        locationService.deleteByLocationName(locationName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocation(@PathVariable Long id,
                                                           @RequestBody @Valid LocationRequest locationRequest) {
        Location location = locationService.updateLocation(id, locationRequest);
        Long venueCount = locationService.countVenuesByLocationId(id);
        return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocationNameById(@PathVariable Long id,
                                                                   @RequestParam(name = "new-location-name")
                                                                   @NotNull @NotBlank String newLocationName) {
        Location location = locationService.updateLocationNameById(id, newLocationName);
        Long venueCount = locationService.countVenuesByLocationName(newLocationName);
        return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
    }

    @PatchMapping("/name/{currentLocationName}")
    public ResponseEntity<LocationResponse> updateLocationNameByLocationName(@PathVariable String currentLocationName,
                                                                             @RequestParam(name = "new-location-name")
                                                                             @NotNull @NotBlank String newLocationName) {
        Location location = locationService.updateLocationNameByLocationName(currentLocationName, newLocationName);
        Long venueCount = locationService.countVenuesByLocationName(newLocationName);
        return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
    }

    @PatchMapping("{id}/venues")
    public ResponseEntity<LocationResponse> updateVenueCountById(@PathVariable Long id,
                                                                 @RequestParam(name = "new-venue-count")
                                                                 @Min(1) Long newVenueCount) {
        Location location = locationService.updateVenueCountById(id, newVenueCount);
        Long venueCount = locationService.countVenuesByLocationId(id);
        return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
    }

    @PatchMapping("name/{locationName}/venues")
    public ResponseEntity<LocationResponse> updateVenueCountByLocationName(@PathVariable String locationName,
                                                                           @RequestParam(name = "new-venue-count")
                                                                           @Min(1) Long newVenueCount) {
        Location location = locationService.updateVenueCountByLocationName(locationName, newVenueCount);
        Long venueCount = locationService.countVenuesByLocationName(locationName);
        return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }
}
