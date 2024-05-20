package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.requests.tournament.TournamentCreationRequest;
import com.fmi.sporttournament.Dto.responses.tournament.TournamentResponse;

import com.fmi.sporttournament.entity.Tournament;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TournamentMapper {
    @Mapping(target = "id", ignore = true)
    Tournament requestToTournament(TournamentCreationRequest tournamentCreationRequest);

    @Mapping(source = "location.id", target = "locationId")
    TournamentResponse tournamentToResponse(Tournament tournament);
}
