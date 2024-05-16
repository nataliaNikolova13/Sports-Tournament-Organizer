package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.Participant;
import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.entity.User;
import com.fmi.sporttournament.entity.enums.ParticipantStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, Long> {
    boolean existsByUserAndTeam(User user, Team team);
    boolean existsByUserIdAndTeamIdAndStatus(Long userId, Long teamId, ParticipantStatus status);
    List<Participant> findByUser(User user);
}
