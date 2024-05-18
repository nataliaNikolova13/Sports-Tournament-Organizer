package com.fmi.sporttournament.entity;
;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "venues")
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venue {

    @EmbeddedId
    private VenueId id;

    @ManyToOne
    @JoinColumn(name = "location_id", insertable = false, updatable = false)
    private Location location;


}
