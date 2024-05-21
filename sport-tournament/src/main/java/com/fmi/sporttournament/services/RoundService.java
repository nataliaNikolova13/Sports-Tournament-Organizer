package com.fmi.sporttournament.services;

import com.fmi.sporttournament.entity.Round;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.repositories.RoundRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class RoundService {
    private final RoundRepository roundRepository;

    public Round createRound(Tournament tournament, int roundNumber){
        Round round = new Round();
        round.setTournament(tournament);
        round.setRoundNumber(roundNumber);
        return roundRepository.save(round);
    }
}
