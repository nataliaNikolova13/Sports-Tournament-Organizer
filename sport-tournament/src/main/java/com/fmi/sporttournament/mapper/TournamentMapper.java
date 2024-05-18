package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.TournamentDto;
import com.fmi.sporttournament.Dto.UserDto;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TournamentMapper {
    TournamentDto tournamentToDto(Tournament tournament);

    Tournament tournamentFromDto(TournamentDto tournamentDto);

    List<TournamentDto> tournamentsToTournamentDtos(List<Tournament> tournaments);
}
