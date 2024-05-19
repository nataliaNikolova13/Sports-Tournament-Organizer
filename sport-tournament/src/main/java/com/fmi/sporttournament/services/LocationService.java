package com.fmi.sporttournament.services;

import com.fmi.sporttournament.entity.Location;

import com.fmi.sporttournament.repositories.LocationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public Optional<Location> getLocationByTournamentName(String tournamentName) {
        return locationRepository.findByLocationName(tournamentName);
    }
    public void removeLocation(Long id) {
        locationRepository.deleteById(id);
    }
}
