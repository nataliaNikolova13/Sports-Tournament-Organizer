package com.fmi.sporttournament.entity;

import com.fmi.sporttournament.entity.enums.MatchResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Table(name = "matches")
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "team_1", nullable = false)
    private Team team_1;

    @ManyToOne
    @JoinColumn(name = "team_2", nullable = false)
    private Team team_2;

    @Column
    private Long round;

    @Column(updatable = false, name = "start_at")
    private Date startAt;

    @Column(updatable = false, name = "end_at")
    private Date endAt;

    @Enumerated(EnumType.STRING)
    @Column(name="match_result")
    private MatchResult matchResult;
}
