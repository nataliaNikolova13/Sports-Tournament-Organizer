package com.fmi.sporttournament.location.controller;

import com.fmi.sporttournament.location.dto.request.LocationRequest;
import com.fmi.sporttournament.location.dto.response.LocationResponse;

import com.fmi.sporttournament.location.entity.Location;

import com.fmi.sporttournament.location.mapper.LocationMapper;

import com.fmi.sporttournament.location.service.LocationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
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

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable Long id) {
        Optional<Location> locationOptional = locationService.getLocationById(id);
        Long venueCount = locationService.countVenuesByLocationId(id);
        return locationOptional.map(
                location -> new ResponseEntity<>(locationMapper.locationToResponse(location, venueCount), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/name/{locationName}")
    public ResponseEntity<LocationResponse> getLocationByName(@PathVariable String locationName) {
        Optional<Location> locationOptional = locationService.getLocationByName(locationName);
        Long venueCount = locationService.countVenuesByLocationName(locationName);
        return locationOptional.map(
                location -> new ResponseEntity<>(locationMapper.locationToResponse(location, venueCount), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(@RequestBody LocationRequest locationRequest) {
        try {
            Location location = locationService.createLocation(locationRequest);
            Long locationId = location.getId();
            Long venueCount = locationService.countVenuesByLocationId(locationId);
            return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocationById(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/name/{locationName}")
    public ResponseEntity<Void> deleteLocationByLocationName(@PathVariable String locationName) {
        try {
            locationService.deleteByLocationName(locationName);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocation(@PathVariable Long id,
                                                           @RequestBody LocationRequest locationRequest) {
        try {
            Location location = locationService.updateLocation(id, locationRequest);
            Long venueCount = locationService.countVenuesByLocationId(id);
            return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocationNameById(@PathVariable Long id,
                                                                   @RequestParam(name = "new-location-name")
                                                                   String newLocationName) {
        try {
            Location location = locationService.updateLocationNameById(id, newLocationName);
            Long venueCount = locationService.countVenuesByLocationName(newLocationName);
            return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/name/{currentLocationName}")
    public ResponseEntity<LocationResponse> updateLocationNameByLocationName(@PathVariable String currentLocationName,
                                                                             @RequestParam(name = "new-location-name")
                                                                             String newLocationName) {
        try {
            Location location = locationService.updateLocationNameByLocationName(currentLocationName, newLocationName);
            Long venueCount = locationService.countVenuesByLocationName(newLocationName);
            return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("{id}/count")
    public ResponseEntity<LocationResponse> updateVenueCountById(@PathVariable Long id,
                                                                 @RequestParam(name = "new-venue-count")
                                                                 Long newVenueCount) {
        try {
            Location location = locationService.updateVenueCountById(id, newVenueCount);
            Long venueCount = locationService.countVenuesByLocationId(id);
            return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("name/{locationName}/count")
    public ResponseEntity<LocationResponse> updateVenueCountByLocationName(@PathVariable String locationName,
                                                                           @RequestParam(name = "new-venue-count")
                                                                           Long newVenueCount) {
        try {
            Location location = locationService.updateVenueCountByLocationName(locationName, newVenueCount);
            Long venueCount = locationService.countVenuesByLocationName(locationName);
            return ResponseEntity.ok(locationMapper.locationToResponse(location, venueCount));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
