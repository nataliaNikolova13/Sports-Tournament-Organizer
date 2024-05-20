package com.fmi.sporttournament.Dto.responses.tournament;

import com.fmi.sporttournament.entity.enums.TournamentParticipantStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class TournamentParticipantResponse {
    private final Long tournamentId;
    private final Long teamId;
    private TournamentParticipantStatus status;
    private Date timeStamp;
}
