package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.responses.ParticipantResponse;
import com.fmi.sporttournament.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "team.id", target = "teamId")
    ParticipantResponse participantToResponse(Participant participant);
}
