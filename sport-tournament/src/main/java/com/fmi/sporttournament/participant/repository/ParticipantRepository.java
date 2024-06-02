package com.fmi.sporttournament.participant.repository;

import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByUserAndTeam(User user, Team team);
    boolean existsByUserIdAndTeamIdAndStatus(Long userId, Long teamId, ParticipantStatus status);
    List<Participant> findByUser(User user);
}
