package com.fmi.sporttournament.Dto.requests.tournament;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DateRequest {
    private Date startAt;
    private Date endAt;
}