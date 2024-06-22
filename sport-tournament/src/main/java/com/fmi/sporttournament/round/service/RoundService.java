package com.fmi.sporttournament.round.service;

import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.match.repository.MatchRepository;
import com.fmi.sporttournament.match_result.entity.MatchResult;
import com.fmi.sporttournament.match_result.repository.MatchResultRepository;
import com.fmi.sporttournament.round.dto.request.RoundRequest;
import com.fmi.sporttournament.round.dto.response.MatchDetailsDto;
import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.round.mapper.RoundMapper;
import com.fmi.sporttournament.round.repository.RoundRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class RoundService {
    private final RoundRepository roundRepository;
    private final RoundMapper roundMapper;
    private final MatchRepository matchRepository;
    private final MatchResultRepository matchResultRepository;
    private final RoundValidationService roundValidationService;

    public Round getRoundById(Long id) {
        return roundValidationService.validateRoundExistById(id);
    }

    public List<Round> getRoundByTournamentId(Long id){
        return roundRepository.findByTournamentId(id);
    }

    public Round createRound(RoundRequest roundRequest) {
        Round round = roundMapper.requestToRound(roundRequest);
        return roundRepository.save(round);
    }

    public List<MatchDetailsDto> getMatchesByRoundId(Long roundId) {
        List<Match> matches = matchRepository.findByRoundId(roundId);
        List<MatchDetailsDto> matchDetailsList = new ArrayList<>();

        for (Match match : matches) {
            MatchResult matchResult = matchResultRepository.findByMatchId(match.getId());

            Integer scoreTeam1 = matchResult != null ? matchResult.getScoreTeam1() : null;
            Integer scoreTeam2 = matchResult != null ? matchResult.getScoreTeam2() : null;
            String winningTeamName = matchResult != null ? matchResult.getWinningTeam().getName() : null;

            MatchDetailsDto matchDetailsDto = new MatchDetailsDto(
                    match.getTeam1().getName(),
                    match.getTeam2().getName(),
                    scoreTeam1,
                    scoreTeam2,
                    winningTeamName
            );
            matchDetailsList.add(matchDetailsDto);
        }

        return matchDetailsList;
    }
}
