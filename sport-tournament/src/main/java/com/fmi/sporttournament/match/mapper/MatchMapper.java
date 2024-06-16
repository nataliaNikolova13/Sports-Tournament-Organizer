package com.fmi.sporttournament.match.mapper;
import com.fmi.sporttournament.match.dto.request.MatchCreationRequest;
import com.fmi.sporttournament.match.dto.response.MatchResponse;
import com.fmi.sporttournament.match.entity.Match;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {
    @Mapping(source = "round.roundNumber", target = "roundNumber")
    @Mapping(source = "team1.name", target = "teamName1")
    @Mapping(source = "team2.name", target = "teamName2")
    @Mapping(source = "venue.location.locationName", target = "locationName")
    @Mapping(source = "venue.number", target = "venueNumber")
    MatchResponse matchToResponse(Match match);
    @Mapping(target = "id", ignore = true)
    Match requestToMatch(MatchCreationRequest matchCreationRequest);
    @Mapping(source = "round.roundNumber", target = "roundNumber")
    @Mapping(source = "team1.name", target = "teamName1")
    @Mapping(source = "team2.name", target = "teamName2")
    @Mapping(source = "venue.location.locationName", target = "locationName")
    @Mapping(source = "venue.number", target = "venueNumber")
    List<MatchResponse> matchesToResponse(List<Match> matches);
}
