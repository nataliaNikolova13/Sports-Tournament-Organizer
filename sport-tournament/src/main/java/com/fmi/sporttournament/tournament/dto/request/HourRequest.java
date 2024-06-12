package com.fmi.sporttournament.tournament.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HourRequest {
    private Integer startHour;
    private Integer endHour;
}
