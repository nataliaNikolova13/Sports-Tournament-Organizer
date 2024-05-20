package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.responses.tournament.TournamentParticipantResponse;

import com.fmi.sporttournament.entity.TournamentParticipant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentParticipantMapper {
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "team.id", target = "teamId")
    TournamentParticipantResponse tournamentParticipantToResponse(TournamentParticipant tournamentParticipant);
}
