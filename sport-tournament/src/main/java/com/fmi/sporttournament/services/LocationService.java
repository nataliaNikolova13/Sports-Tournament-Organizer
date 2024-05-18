package com.fmi.sporttournament.services;

import com.fmi.sporttournament.entity.Location;
import com.fmi.sporttournament.entity.Venue;
import com.fmi.sporttournament.repositories.LocationRepository;
import com.fmi.sporttournament.repositories.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<Location> getLocationByTournamentName(String tournamentName) {
        return locationRepository.findByLocationName(tournamentName);
    }

}
