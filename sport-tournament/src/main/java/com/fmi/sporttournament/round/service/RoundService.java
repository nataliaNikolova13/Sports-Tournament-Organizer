package com.fmi.sporttournament.round.service;

import com.fmi.sporttournament.round.dto.request.RoundRequest;
import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.round.mapper.RoundMapper;
import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.round.repository.RoundRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class RoundService {
    private final RoundRepository roundRepository;
    private final RoundMapper roundMapper;

    public Optional<Round> getRoundById(Long id){
        return roundRepository.findById(id);
    }

    public Round createRound(RoundRequest roundRequest){
        Round round = roundMapper.requestToRound(roundRequest);
        return roundRepository.save(round);
    }
}
