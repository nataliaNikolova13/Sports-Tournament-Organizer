package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.requests.tournament.LocationRequest;
import com.fmi.sporttournament.Dto.responses.tournament.LocationResponse;

import com.fmi.sporttournament.entity.Location;

import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.mapper.LocationMapper;

import com.fmi.sporttournament.services.LocationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return locationOptional.map(location -> new ResponseEntity<>(locationMapper.locationToResponse(location), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create-location")
    public ResponseEntity<LocationResponse> createLocation(@RequestBody LocationRequest locationRequest) {
        try{
            Location location = locationService.createLocation(locationRequest);
            return ResponseEntity.ok(locationMapper.locationToResponse(location));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{locationName}")
    public  ResponseEntity<LocationResponse> getLocationByName(@PathVariable String locationName){
        Optional<Location> locationOptional = locationService.getLocationByName(locationName);
        return locationOptional.map(location -> new ResponseEntity<>(locationMapper.locationToResponse(location), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLocation(@PathVariable Long id) {
        locationService.removeLocation(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/name/{locationName}")
    public ResponseEntity<Void> removeLocation(@PathVariable String locationName) {
        locationService.removeByLocationName(locationName);
        return ResponseEntity.ok().build();
    }
}
