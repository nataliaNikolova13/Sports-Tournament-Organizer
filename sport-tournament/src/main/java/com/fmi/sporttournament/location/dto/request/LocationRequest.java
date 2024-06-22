package com.fmi.sporttournament.location.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationRequest {
    @NotNull
    @NotBlank
    private String locationName;

    @NotNull
    @Min(1)
    private Long venueCount;
}
