package com.fmi.sporttournament.Dto.responses.tournament;

import com.fmi.sporttournament.entity.enums.TournamentParticipantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TournamentParticipantResponse {
    private Long tournamentId;
    private Long teamId;
    private TournamentParticipantStatus status;
    private Date timeStamp;
}
