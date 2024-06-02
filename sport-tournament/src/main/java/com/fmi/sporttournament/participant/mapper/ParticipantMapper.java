package com.fmi.sporttournament.participant.mapper;

import com.fmi.sporttournament.participant.dto.response.ParticipantResponse;
import com.fmi.sporttournament.participant.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "team.id", target = "teamId")
    ParticipantResponse participantToResponse(Participant participant);
}
