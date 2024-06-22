package com.fmi.sporttournament.tournament.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DateRequest {
    @NotNull
    @Future
    private Date startAt;

    @NotNull
    @Future
    private Date endAt;
}
