package com.fmi.sporttournament.participant.repository;

import com.fmi.sporttournament.participant.entity.Participant;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.participant.entity.status.ParticipantStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByUserAndTeam(User user, Team team);
    boolean existsByUserIdAndTeamIdAndStatus(Long userId, Long teamId, ParticipantStatus status);
    List<Participant> findByUser(User user);
    Optional<Participant> findByUserAndTeam(User user, Team team);
    @Query("SELECT COUNT(p) FROM Participant p WHERE p.team = :team AND p.status = :status")
    int countParticipantsByTeamAndStatus(Team team, ParticipantStatus status);

    @Query("SELECT p.team FROM Participant p WHERE p.user = :user AND p.status = 'joined'")
    List<Team> findTeamsByUser(User user);

    @Query("SELECT p.user FROM Participant p WHERE p.team = :team AND p.status = 'joined'")
    List<User> findUsersByTeam(Team team);

    @Query("SELECT DISTINCT p.team FROM Participant p WHERE p.status = 'joined'")
    List<Team> findTeamsWithJoinedParticipants();
}
