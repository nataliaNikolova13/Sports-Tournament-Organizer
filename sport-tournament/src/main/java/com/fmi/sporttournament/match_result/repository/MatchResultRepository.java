package com.fmi.sporttournament.match_result.repository;

import com.fmi.sporttournament.match_result.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
}
