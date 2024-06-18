package com.fmi.sporttournament.match.service;

import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;

import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.match.repository.MatchRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchValidationService {
    private final MatchRepository matchRepository;

    public Match validateMatchExistById(Long id) {
        Optional<Match> match = matchRepository.findById(id);
        if (match.isEmpty()) {
            throw new ResourceNotFoundException("The match with id " + id + " doesn't exist");
        }
        return match.get();
    }
}
