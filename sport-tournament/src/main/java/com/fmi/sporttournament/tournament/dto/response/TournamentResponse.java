package com.fmi.sporttournament.tournament.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TournamentResponse {
    private String tournamentName;
    private String sportType;
    private Long locationId;
    private Date startAt;
    private Date endAt;
    private Integer startHour;
    private Integer endHour;
    private Integer teamCount;
    private Integer matchDuration;
    private Integer teamMemberCount;
}
