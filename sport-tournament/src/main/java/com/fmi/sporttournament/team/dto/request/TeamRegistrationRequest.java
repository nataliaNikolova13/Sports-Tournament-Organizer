package com.fmi.sporttournament.team.dto.request;

import com.fmi.sporttournament.team.entity.category.TeamCategory;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamRegistrationRequest {
    private String name;
    private TeamCategory category;
}