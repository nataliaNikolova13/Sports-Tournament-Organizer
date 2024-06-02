package com.fmi.sporttournament.team.mapper;

import com.fmi.sporttournament.team.dto.request.TeamRegistrationRequest;
import com.fmi.sporttournament.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamRegistrationRequest teamToDto(Team team);
    @Mapping(target = "id", ignore = true)
    Team dtoToTeam(TeamRegistrationRequest teamRegistrationRequest);
}
