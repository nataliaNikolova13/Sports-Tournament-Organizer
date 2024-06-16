package com.fmi.sporttournament.team.mapper;

import com.fmi.sporttournament.team.dto.request.TeamRequest;
import com.fmi.sporttournament.team.dto.response.TeamResponse;
import com.fmi.sporttournament.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamResponse teamToResponse(Team team);
    @Mapping(target = "id", ignore = true)
    Team requestToTeam(TeamRequest teamRequest);
}
