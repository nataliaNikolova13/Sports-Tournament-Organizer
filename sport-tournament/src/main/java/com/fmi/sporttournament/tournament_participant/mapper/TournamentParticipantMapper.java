package com.fmi.sporttournament.tournament_participant.mapper;

import com.fmi.sporttournament.tournament_participant.dto.response.TournamentParticipantResponse;

import com.fmi.sporttournament.tournament_participant.entity.TournamentParticipant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentParticipantMapper {
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "team.id", target = "teamId")
    TournamentParticipantResponse tournamentParticipantToResponse(TournamentParticipant tournamentParticipant);
}
