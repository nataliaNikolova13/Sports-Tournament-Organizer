package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.MatchDto;

import com.fmi.sporttournament.entity.Match;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {
    MatchDto matchToDto(Match match);

    Match matchFromDto(MatchDto matchDto);

    List<MatchDto> matchesToMatchDtos(List<Match> matches);
}
