package com.fmi.sporttournament.round.service;

import com.fmi.sporttournament.round.dto.request.RoundRequest;
import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.round.mapper.RoundMapper;
import com.fmi.sporttournament.round.repository.RoundRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class RoundService {
    private final RoundRepository roundRepository;
    private final RoundMapper roundMapper;
    private final RoundValidationService roundValidationService;

    public Round getRoundById(Long id) {
        return roundValidationService.validateRoundExistById(id);
    }

    public Round createRound(RoundRequest roundRequest) {
        Round round = roundMapper.requestToRound(roundRequest);
        return roundRepository.save(round);
    }
}
