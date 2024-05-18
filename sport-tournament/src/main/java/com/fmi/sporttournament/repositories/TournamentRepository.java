package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.User;
import com.fmi.sporttournament.entity.enums.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface TournamentRepository extends CrudRepository<Tournament, Long> {
    Optional<Tournament> findByTournamentName(String tournamentName);
    Optional<Tournament> findById(Long id);
    boolean existsByTournamentName(String tournamentName);
    List<Tournament> findBySportType(String sportType); // Derived query method

    @Query("SELECT t FROM Tournament t WHERE t.location.locationName = :locationName")
    List<Tournament> findByLocation(String locationName); // Derived query method

    // Optional: This method may be unnecessary if you don't have additional filtering or sorting requirements
    List<Tournament> findAll();

}
