package com.fmi.sporttournament.participant.dto.response;

import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ParticipantResponse {
    private Long userId;
    private Long teamId;
    private ParticipantStatus status;
    private Date timeStamp;
}
