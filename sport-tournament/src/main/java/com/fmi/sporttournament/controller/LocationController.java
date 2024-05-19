package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.LocationDto;
import com.fmi.sporttournament.entity.Location;
import com.fmi.sporttournament.mapper.LocationMapper;
import com.fmi.sporttournament.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<LocationDto> getLocationById(@PathVariable Long id) {
        Optional<Location> locationOptional = locationService.getLocationById(id);
        return locationOptional.map(location -> new ResponseEntity<>(locationMapper.locationToDto(location), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tournament/{tournamentName}")
    public ResponseEntity<LocationDto> getLocationByTournamentName(@PathVariable String tournamentName) {
        Optional<Location> locationOptional = locationService.getLocationByTournamentName(tournamentName);
        return locationOptional.map(location -> new ResponseEntity<>(locationMapper.locationToDto(location), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<Location> allLocations = locationService.getAllLocations();
        return new ResponseEntity<>(locationMapper.locationsToLocationDtos(allLocations), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLocation(@PathVariable Long id) {
        locationService.removeLocation(id);
        return ResponseEntity.ok().build();
    }
}
