package com.fmi.sporttournament.round.service;

import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;
import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.round.repository.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoundValidationService {
    private final RoundRepository roundRepository;

    public Round validateRoundExistById(Long id) {
        Optional<Round> round = roundRepository.findById(id);
        if (round.isEmpty()) {
            throw new ResourceNotFoundException("The round with id " + id + "doesn't exist");
        }
        return round.get();
    }
}
