package com.fmi.sporttournament.match_result.service;

import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;
import com.fmi.sporttournament.match_result.entity.MatchResult;
import com.fmi.sporttournament.match_result.repository.MatchResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchResultValidationService {
    private final MatchResultRepository matchResultRepository;
    public MatchResult validateMatchResultExistById(Long id) {
        Optional<MatchResult> matchResult = matchResultRepository.findById(id);
        if (matchResult.isEmpty()) {
            throw new ResourceNotFoundException("The match result with id " + id + " doesn't exist");
        }
        return matchResult.get();
    }
}
