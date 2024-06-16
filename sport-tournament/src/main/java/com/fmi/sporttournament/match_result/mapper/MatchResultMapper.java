package com.fmi.sporttournament.match_result.mapper;

import com.fmi.sporttournament.match_result.dto.request.MatchResultRequest;
import com.fmi.sporttournament.match_result.dto.response.MatchResultResponse;
import com.fmi.sporttournament.match_result.entity.MatchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchResultMapper {
    @Mapping(source = "match.id", target = "matchId")
    @Mapping(source = "match.round.roundNumber", target = "roundNumber")
    @Mapping(source = "match.team1.name", target = "teamName1")
    @Mapping(source = "match.team2.name", target = "teamName2")
    @Mapping(source = "winningTeam.name", target = "winningTeamName")
    MatchResultResponse matchResultToResponse(MatchResult matchResult);

    @Mapping(target = "id", ignore = true)
    MatchResult requestToMatchResult(MatchResultRequest matchResultRequest);
    @Mapping(source = "match.id", target = "matchId")
    @Mapping(source = "match.round.roundNumber", target = "roundNumber")
    @Mapping(source = "match.team1.name", target = "teamName1")
    @Mapping(source = "match.team2.name", target = "teamName2")
    @Mapping(source = "winningTeam.name", target = "winningTeamName")
    List<MatchResultResponse> matchResultsToResponse(List<MatchResult> matchResult);
}
