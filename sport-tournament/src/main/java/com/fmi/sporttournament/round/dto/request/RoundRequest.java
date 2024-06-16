package com.fmi.sporttournament.round.dto.request;

import com.fmi.sporttournament.tournament.entity.Tournament;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoundRequest {
    private Tournament tournament;
    private Integer roundNumber;
}
