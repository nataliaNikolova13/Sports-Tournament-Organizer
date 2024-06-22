package com.fmi.sporttournament.tournament.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HourRequest {
    @NotNull
    @Min(0)
    @Max(23)
    private Integer startHour;

    @NotNull
    @Min(1)
    @Max(24)
    private Integer endHour;
}
