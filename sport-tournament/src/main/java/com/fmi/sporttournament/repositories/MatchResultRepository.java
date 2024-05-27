package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
}
