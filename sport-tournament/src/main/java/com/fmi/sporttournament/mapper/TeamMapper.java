package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.requests.TeamRegistrationRequest;
import com.fmi.sporttournament.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamRegistrationRequest teamToDto(Team team);
    @Mapping(target = "id", ignore = true)
    Team dtoToTeam(TeamRegistrationRequest teamRegistrationRequest);
}
