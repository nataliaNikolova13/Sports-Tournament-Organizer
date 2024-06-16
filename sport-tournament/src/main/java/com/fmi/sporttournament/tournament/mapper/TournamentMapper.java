package com.fmi.sporttournament.tournament.mapper;

import com.fmi.sporttournament.tournament.dto.request.TournamentCreationRequest;
import com.fmi.sporttournament.tournament.dto.response.TournamentResponse;

import com.fmi.sporttournament.tournament.entity.Tournament;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface TournamentMapper {
    @Mapping(target = "id", ignore = true)
    Tournament requestToTournament(TournamentCreationRequest tournamentCreationRequest);

    @Mapping(source = "location.locationName", target = "locationName")
    TournamentResponse tournamentToResponse(Tournament tournament);

    @Mapping(source = "location.locationName", target = "locationName")
    List<TournamentResponse> tournamentsToResponse(List<Tournament> tournaments);

}
