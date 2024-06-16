package com.fmi.sporttournament.round.mapper;

import com.fmi.sporttournament.round.dto.request.RoundRequest;
import com.fmi.sporttournament.round.dto.response.RoundResponse;
import com.fmi.sporttournament.round.entity.Round;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoundMapper {
    RoundResponse roundToResponse(Round round);

    @Mapping(target = "id", ignore = true)
    Round requestToRound(RoundRequest roundRequest);
}
