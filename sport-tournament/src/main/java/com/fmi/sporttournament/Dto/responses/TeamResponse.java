package com.fmi.sporttournament.Dto.responses;

import com.fmi.sporttournament.entity.enums.TeamCategory;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TeamResponse {
    private String name;
    private TeamCategory category;
}