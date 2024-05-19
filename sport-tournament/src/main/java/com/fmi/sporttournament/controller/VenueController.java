package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.VenueDto;
import com.fmi.sporttournament.entity.Venue;
import com.fmi.sporttournament.mapper.VenueMapper;
import com.fmi.sporttournament.services.VenueService;
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
@RequestMapping("/venue")
public class VenueController {
    private final VenueService venueService;
    private final VenueMapper venueMapper;

    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> getVenueById(@PathVariable Long id) {
        Optional<Venue> venue = venueService.getVenueById(id);
        return venue.map(value -> new ResponseEntity<>(venueMapper.venueToDto(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/location/{locationName}")
    public ResponseEntity<List<VenueDto>> getVenueByLocation(@PathVariable String locationName) {
        List<Venue> venues = venueService.getAllVenuesInLocation(locationName);
        return new ResponseEntity<>(venueMapper.venuesToVenuesDtos(venues), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<VenueDto>> getAllVenues() {
        List<Venue> allVenues = venueService.getAllVenues();
        return new ResponseEntity<>(venueMapper.venuesToVenuesDtos(allVenues), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeVenue(@PathVariable Long id) {
        venueService.removeVenue(id);
        return ResponseEntity.ok().build();
    }
}
