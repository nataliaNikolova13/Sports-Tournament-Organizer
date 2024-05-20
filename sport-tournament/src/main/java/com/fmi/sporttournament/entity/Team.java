package com.fmi.sporttournament.entity;

import com.fmi.sporttournament.entity.enums.TeamCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

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
