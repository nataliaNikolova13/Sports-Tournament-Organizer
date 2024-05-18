package com.fmi.sporttournament.services;

import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.Venue;
import com.fmi.sporttournament.entity.VenueId;
import com.fmi.sporttournament.repositories.TournamentRepository;
import com.fmi.sporttournament.repositories.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;

    public List<Venue> getAllVenuesInLocation(String locationName) {
        return venueRepository.findByLocation_LocationName(locationName);
    }

    public Optional<Venue> getVenueByIdAndLocation(VenueId venueId, String locationName) {
        return venueRepository.findByIdAndLocation_LocationName(venueId, locationName);
    }

}
