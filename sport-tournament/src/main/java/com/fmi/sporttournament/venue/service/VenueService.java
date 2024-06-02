package com.fmi.sporttournament.venue.service;

import com.fmi.sporttournament.venue.dto.request.VenueRequest;

import com.fmi.sporttournament.venue.entity.Venue;

import com.fmi.sporttournament.venue.mapper.VenueMapper;
import com.fmi.sporttournament.venue.repository.VenueRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Optional<Venue> getVenueById(Long id) {
        return venueRepository.findById(id);
    }

    public List<Venue> getAllVenuesInLocation(String locationName) {
        return venueRepository.findByLocationLocationName(locationName);
    }

    public Venue createVenue(VenueRequest venueRequest) {
        Venue venue = venueMapper.requestToVenue(venueRequest);
        return venueRepository.save(venue);
    }

    public void deleteVenueById(Long id) {
        venueRepository.deleteById(id);
    }

    public void deleteExcessVenues(String locationName, Long currentVenueCount, Long newVenueCount) {
        if (currentVenueCount > newVenueCount) {
            for (long i = newVenueCount + 1; i <= currentVenueCount; i++) {
                System.out.println(i);
                venueRepository.deleteByLocationLocationNameAndNumber(locationName, i);
            }
        }
    }
}
