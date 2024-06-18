package com.fmi.sporttournament.tournament.dto.request;

import com.fmi.sporttournament.tournament.entity.category.TournamentCategory;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TournamentRegistrationRequest {
    @NotNull
    @NotBlank
    private String tournamentName;

    @NotNull
    @NotBlank
    private String sportType;

    @NotNull
    private TournamentCategory tournamentCategory;

    @NotNull
    @NotBlank
    private String locationName;

    @NotNull
    @Future
    private Date startAt;

    @NotNull
    @Future
    private Date endAt;

    @NotNull
    @Min(0)
    @Max(23)
    private Integer startHour;

    @NotNull
    @Min(1)
    @Max(24)
    private Integer endHour;

    @NotNull
    @Min(2)
    private Integer teamCount;

    @NotNull
    @Min(1)
    @Max(23)
    private Integer matchDuration;

    @NotNull
    @Min(1)
    private Integer teamMemberCount;
}
