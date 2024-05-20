package com.fmi.sporttournament.services;

import com.fmi.sporttournament.Dto.requests.tournament.VenueRequest;
import com.fmi.sporttournament.entity.Venue;

import com.fmi.sporttournament.mapper.VenueMapper;
import com.fmi.sporttournament.repositories.VenueRepository;

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

    public void removeVenue(Long id) {
        venueRepository.deleteById(id);
    }

    public Venue createVenue(VenueRequest venueRequest){
        Venue venue = venueMapper.requestToVenue(venueRequest);
        return venueRepository.save(venue);
    }
}
