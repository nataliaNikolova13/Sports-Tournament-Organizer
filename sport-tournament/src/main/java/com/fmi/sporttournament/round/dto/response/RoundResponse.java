package com.fmi.sporttournament.round.dto.response;

import com.fmi.sporttournament.tournament.entity.Tournament;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoundResponse {
    private Tournament tournament;
    private Integer roundNumber;
}
