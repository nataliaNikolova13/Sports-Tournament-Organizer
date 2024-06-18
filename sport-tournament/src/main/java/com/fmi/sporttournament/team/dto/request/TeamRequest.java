package com.fmi.sporttournament.team.dto.request;

import com.fmi.sporttournament.team.entity.category.TeamCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamRequest {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private TeamCategory category;
}