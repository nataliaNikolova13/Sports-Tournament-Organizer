package com.fmi.sporttournament.Dto.responses;

import com.fmi.sporttournament.entity.enums.TeamCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamResponse {
    private String name;
    private TeamCategory category;
}