package com.fmi.sporttournament.participant.entity;

import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ParticipantStatus status;

    @CreationTimestamp
    @Column(updatable = false, name = "time_stamp")
    private Date timeStamp;
}
