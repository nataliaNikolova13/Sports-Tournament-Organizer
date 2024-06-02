package com.fmi.sporttournament.team.dto.response;

import com.fmi.sporttournament.team.entity.category.TeamCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamResponse {
    private String name;
    private TeamCategory category;
}