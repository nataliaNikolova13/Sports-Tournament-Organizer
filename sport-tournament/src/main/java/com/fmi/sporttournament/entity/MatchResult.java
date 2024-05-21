package com.fmi.sporttournament.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "match_results")
@Data
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "winning_team_id", nullable = false)
    private Team winningTeam;

    @Column(name = "score_team1", nullable = false)
    private int scoreTeam1;

    @Column(name = "score_team2", nullable = false)
    private int scoreTeam2;
}
