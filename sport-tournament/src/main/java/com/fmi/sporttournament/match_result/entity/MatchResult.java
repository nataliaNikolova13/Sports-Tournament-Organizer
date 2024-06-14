package com.fmi.sporttournament.match_result.entity;

import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.team.entity.Team;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "winning_team_id", nullable = false)
    private Team winningTeam;

    @Column(name = "score_team1", nullable = false)
    private int scoreTeam1;

    @Column(name = "score_team2", nullable = false)
    private int scoreTeam2;
}
