package com.fmi.sporttournament.tournament.repository;

import com.fmi.sporttournament.location.entity.Location;
import com.fmi.sporttournament.tournament.entity.Tournament;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Optional<Tournament> findByTournamentName(String tournamentName);

    Optional<Tournament> findById(Long id);

    List<Tournament> findBySportType(String sportType);

    List<Tournament> findByLocationLocationName(String locationName);

    List<Tournament> findAll();

    @Query("SELECT t FROM Tournament t WHERE t.location.id = :locationId AND " +
        "(t.startAt <= :endAt AND t.endAt >= :startAt)")
    List<Tournament> findConflictingTournaments(@Param("locationId") Long locationId,
                                                @Param("startAt") Date startAt,
                                                @Param("endAt") Date endAt);

    @Query("SELECT t FROM Tournament t WHERE t.location= :location AND t.endAt > CURRENT_TIMESTAMP")
    List<Tournament> findValidTournamentsByLocation(Location location);

    @Query("SELECT t FROM Tournament t WHERE t.sportType= :sportType AND t.endAt > CURRENT_TIMESTAMP")
    List<Tournament> findValidTournamentsBySportType(String sportType);

    @Query("SELECT t FROM Tournament t WHERE t.endAt > CURRENT_TIMESTAMP")
    List<Tournament> findValidTournaments();

    List<Tournament> findByStartAt(Date startAt);
}
