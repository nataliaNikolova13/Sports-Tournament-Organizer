package com.fmi.sporttournament.entity;

import com.fmi.sporttournament.entity.enums.TeamCategory;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TeamCategory category;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;
}
