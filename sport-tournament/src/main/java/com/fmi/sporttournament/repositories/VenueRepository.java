package com.fmi.sporttournament.repositories;

import com.fmi.sporttournament.entity.Location;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.Venue;
import com.fmi.sporttournament.entity.VenueId;
import com.fmi.sporttournament.entity.enums.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository

public interface VenueRepository extends CrudRepository<Venue, Long> {
   // @Query("SELECT v FROM Venue v WHERE v.id.id = :v AND v.id.location.locationName = :locationName")
    Optional<Venue> findByIdAndLocation_LocationName(VenueId id, String locationName);
 //   boolean existsByIdAndLocationName(Long id,String locationName);
    @Query("SELECT v FROM Venue v WHERE v.id.location.locationName = :locationName")
    List<Venue> findByLocation_LocationName(String locationName);

}
