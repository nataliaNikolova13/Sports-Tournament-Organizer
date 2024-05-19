package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.TeamResultDto;

import com.fmi.sporttournament.entity.TeamResult;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamResultMapper {
    TeamResultDto teamResultToDto(TeamResult teamResult);

    TeamResult teamResultFromDto(TeamResultDto teamResultDto);

    List<TeamResultDto> teamResultsToTeamResultDtos(List<TeamResult> teamResults);
}
