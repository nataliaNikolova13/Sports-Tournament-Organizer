package com.fmi.sporttournament.venue.controller;
import com.fmi.sporttournament.venue.dto.response.VenueResponse;
import com.fmi.sporttournament.venue.entity.Venue;
import com.fmi.sporttournament.venue.mapper.VenueMapper;
import com.fmi.sporttournament.venue.service.VenueService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venue")
public class VenueController {
    private final VenueService venueService;
    private final VenueMapper venueMapper;

    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id) {
        Venue venue = venueService.getVenueById(id);
        return ResponseEntity.ok(venueMapper.venueToResponse(venue));
    }
}
