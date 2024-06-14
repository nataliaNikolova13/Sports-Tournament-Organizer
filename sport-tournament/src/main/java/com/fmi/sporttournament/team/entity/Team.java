package com.fmi.sporttournament.team.entity;

import com.fmi.sporttournament.team.entity.category.TeamCategory;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name", unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private TeamCategory category;
}
