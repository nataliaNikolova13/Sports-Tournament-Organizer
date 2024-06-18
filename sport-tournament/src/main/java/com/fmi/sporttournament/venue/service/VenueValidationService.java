package com.fmi.sporttournament.venue.service;

import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;

import com.fmi.sporttournament.location.entity.Location;

import com.fmi.sporttournament.venue.entity.Venue;
import com.fmi.sporttournament.venue.repository.VenueRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VenueValidationService {
    private final VenueRepository venueRepository;
    public Venue validateVenueExistById(Long id) {
        Optional<Venue> venue = venueRepository.findById(id);
        if (venue.isEmpty()) {
            throw new ResourceNotFoundException("The venue with id " + id + " doesn't exist");
        }
        return venue.get();
    }

    public Venue validateVenue(Location location, Long number) {
        Optional<Venue> venue = venueRepository.findByLocationAndNumber(location, number);
        if (venue.isEmpty()) {
            throw new ResourceNotFoundException(
                "There isn't such venue at location " + location.getLocationName() + " with number " + number);
        }
        return venue.get();
    }
}
