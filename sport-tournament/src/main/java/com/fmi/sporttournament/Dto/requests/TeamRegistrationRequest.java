package com.fmi.sporttournament.Dto.requests;

import com.fmi.sporttournament.entity.enums.TeamCategory;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamRegistrationRequest {
    private String name;
    private TeamCategory category;
}