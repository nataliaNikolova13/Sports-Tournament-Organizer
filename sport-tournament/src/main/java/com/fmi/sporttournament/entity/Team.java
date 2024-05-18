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

    private String name;

    @Enumerated(EnumType.STRING)
    private TeamCategory category;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @OneToMany(mappedBy = "team_1", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Match> matchesTeam1;

    @OneToMany(mappedBy = "team_2", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Match> matchesTeam2;

    @OneToOne(mappedBy = "team")
    private TeamResult teamResult;
}
