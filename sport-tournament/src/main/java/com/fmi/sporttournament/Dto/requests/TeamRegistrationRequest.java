package com.fmi.sporttournament.Dto.requests;

import com.fmi.sporttournament.entity.enums.TeamCategory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TeamRegistrationRequest {
    private String name;
    private TeamCategory category;
}