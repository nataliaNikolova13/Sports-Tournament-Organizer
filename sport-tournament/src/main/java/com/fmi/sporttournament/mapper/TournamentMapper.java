package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.TournamentDto;

import com.fmi.sporttournament.entity.Tournament;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TournamentMapper {
    TournamentDto tournamentToDto(Tournament tournament);

    Tournament tournamentFromDto(TournamentDto tournamentDto);

    List<TournamentDto> tournamentsToTournamentDtos(List<Tournament> tournaments);
}
