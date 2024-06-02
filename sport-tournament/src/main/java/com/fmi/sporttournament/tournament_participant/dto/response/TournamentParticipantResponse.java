package com.fmi.sporttournament.tournament_participant.dto.response;

import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;
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
