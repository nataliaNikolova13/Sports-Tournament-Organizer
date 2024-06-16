package com.fmi.sporttournament.tournament.dto.request;

import com.fmi.sporttournament.location.entity.Location;
import com.fmi.sporttournament.tournament.entity.category.TournamentCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TournamentCreationRequest {
    private String tournamentName;
    private String sportType;
    private TournamentCategory tournamentCategory;
    private Location location;
    private Date startAt;
    private Date endAt;
    private Integer startHour;
    private Integer endHour;
    private Integer teamCount;
    private Integer matchDuration;
    private Integer teamMemberCount;
}