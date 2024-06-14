package com.fmi.sporttournament.match.entity;

import com.fmi.sporttournament.round.entity.Round;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.venue.entity.Venue;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team1_id", nullable = false)
    private Team team1;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team2_id", nullable = false)
    private Team team2;

    @Column(name = "match_time", nullable = false)
    private LocalDateTime matchTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;
}
