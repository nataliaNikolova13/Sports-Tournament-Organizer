package com.fmi.sporttournament.tournament.entity;

import com.fmi.sporttournament.location.entity.Location;
import com.fmi.sporttournament.tournament.entity.category.TournamentCategory;
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

import java.util.Date;

@Table(name = "tournaments")
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "tournament_name", nullable = false, unique = true)
    private String tournamentName;

    @Column(name = "sport_type", nullable = false)
    private String sportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "tournament_category", nullable = false)
    private TournamentCategory tournamentCategory;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "start_at", nullable = false)
    private Date startAt;

    @Column(name = "end_at", nullable = false)
    private Date endAt;

    @Column(name = "start_hour", nullable = false)
    private Integer startHour;

    @Column(name = "end_hour", nullable = false)
    private Integer endHour;

    @Column(name = "team_count", nullable = false)
    private Integer teamCount;

    @Column(name = "match_duration", nullable = false)
    private Integer matchDuration;

    @Column(name = "team_members_count", nullable = false)
    private Integer teamMemberCount;
}
