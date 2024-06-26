package com.fmi.sporttournament.tournament.controller;

import com.fmi.sporttournament.tournament.dto.request.DateRequest;
import com.fmi.sporttournament.tournament.dto.request.HourRequest;
import com.fmi.sporttournament.tournament.dto.request.TournamentRegistrationRequest;
import com.fmi.sporttournament.tournament.dto.response.TournamentResponse;
import com.fmi.sporttournament.tournament.entity.Tournament;
import com.fmi.sporttournament.tournament.entity.category.TournamentCategory;
import com.fmi.sporttournament.tournament.mapper.TournamentMapper;
import com.fmi.sporttournament.tournament.service.TournamentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournament")
public class TournamentController {
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponse> getTournamentById(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(tournament));
    }

    @GetMapping("/name/{tournamentName}")
    public ResponseEntity<TournamentResponse> getTournamentByTournamentName(@PathVariable String tournamentName) {
        Tournament tournament = tournamentService.getTournamentByTournamentName(tournamentName);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(tournament));
    }

    @GetMapping("/location/{locationName}")
    public ResponseEntity<List<TournamentResponse>> getTournamentsByLocationName(@PathVariable String locationName) {
        List<Tournament> tournaments = tournamentService.getTournamentByLocationName(locationName);
        return ResponseEntity.ok(tournamentMapper.tournamentsToResponse(tournaments));
    }

    @GetMapping("/sport/{sportType}")
    public ResponseEntity<List<TournamentResponse>> getTournamentsBySportType(@PathVariable String sportType) {
        List<Tournament> tournaments = tournamentService.getTournamentBySportType(sportType);
        return ResponseEntity.ok(tournamentMapper.tournamentsToResponse(tournaments));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TournamentResponse>> getAllTournament() {
      List<Tournament> tournament = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournamentMapper.tournamentsToResponse(tournament));
    }

    @GetMapping("/all-active-tournaments")
    public ResponseEntity<List<TournamentResponse>> getAllValidTournament() {
        List<Tournament> tournament = tournamentService.getAllValidTournaments();
        return ResponseEntity.ok(tournamentMapper.tournamentsToResponse(tournament));
    }

    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(
        @RequestBody @Valid TournamentRegistrationRequest tournamentRequest) {
        Tournament tournament = tournamentService.createTournament(tournamentRequest);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(tournament));
    }

    @PostMapping("/start-tournament/{id}")
    public ResponseEntity<TournamentResponse> startTournament(@PathVariable Long id) {
        try {
            Tournament tournament = tournamentService.startTournamentById(id);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(tournament));
        } catch (IOException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournamentById(@PathVariable Long id) {
        tournamentService.deleteTournamentById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/name/{tournamentName}")
    public ResponseEntity<Void> deleteTournamentByTournamentName(@PathVariable String tournamentName) {
        tournamentService.deleteTournamentByTournamentName(tournamentName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponse> updateTournament(@PathVariable Long id,
                                                               @RequestBody @Valid
                                                               TournamentRegistrationRequest tournamentRegistrationRequest) {
        Tournament updatedTournament = tournamentService.updateTournament(id, tournamentRegistrationRequest);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TournamentResponse> updateTournamentNameById(@PathVariable Long id,
                                                                       @RequestParam(name = "new-tournament-name")
                                                                       @NotNull @NotBlank String newTournamentName) {
        Tournament updatedTournament =
            tournamentService.updateTournamentNameById(id, newTournamentName);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/name/{currentTournamentName}")
    public ResponseEntity<TournamentResponse> updateTournamentNameByTournamentName(
        @PathVariable String currentTournamentName,
        @RequestParam(name = "new-tournament-name")
        @NotNull @NotBlank
        String newTournamentName) {
        Tournament updatedTournament =
            tournamentService.updateTournamentNameByTournamentName(currentTournamentName, newTournamentName);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<TournamentResponse> updateTournamentLocationByTId(@PathVariable Long id,
                                                                            @RequestParam(name = "new-tournament-location")
                                                                            @NotNull @NotBlank
                                                                            String newLocationName) {
        Tournament updatedTournament =
            tournamentService.updateTournamentLocationById(id, newLocationName);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/name/{tournamentName}/location")
    public ResponseEntity<TournamentResponse> updateTournamentLocationByTournamentName(
        @PathVariable String tournamentName,
        @RequestParam(name = "new-tournament-location")
        @NotNull @NotBlank String newLocationName) {
        Tournament updatedTournament =
            tournamentService.updateTournamentLocationByTournamentName(tournamentName, newLocationName);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/{id}/sport")
    public ResponseEntity<TournamentResponse> updateTournamentSportTypeById(@PathVariable Long id,
                                                                            @RequestParam(name = "new-sport-type")
                                                                            @NotNull @NotBlank
                                                                            String newSportType) {
        Tournament updatedTournament = tournamentService.updateTournamentSportTypeById(id, newSportType);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/name/{tournamentName}/sport")
    public ResponseEntity<TournamentResponse> updateTournamentSportTypeByTournamentName(
        @PathVariable String tournamentName,
        @RequestParam(name = "new-sport-type")
        @NotNull @NotBlank String newSportType
    ) {
        Tournament updatedTournament =
            tournamentService.updateTournamentSportTypeByTournamentName(tournamentName, newSportType);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/{id}/category")
    public ResponseEntity<TournamentResponse> updateTournamentCategoryById(@PathVariable Long id,
                                                                           @RequestParam(name = "new-category")
                                                                           TournamentCategory newCategory) {
        Tournament updatedTournament = tournamentService.updateTournamentCategoryById(id, newCategory);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/name/{tournamentName}/category")
    public ResponseEntity<TournamentResponse> updateTournamentCategoryByTournamentName(
        @PathVariable String tournamentName,
        @RequestParam(name = "new-category")
        TournamentCategory newCategory
    ) {
        Tournament updatedTournament =
            tournamentService.updateTournamentCategoryByTournamentName(tournamentName, newCategory);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/{id}/dates")
    public ResponseEntity<TournamentResponse> updateTournamentDatesById(@PathVariable Long id,
                                                                        @RequestBody @Valid DateRequest dateRequest) {
        Date newStartAt = dateRequest.getStartAt();
        Date newEndAt = dateRequest.getEndAt();
        Tournament tournament =
            tournamentService.updateTournamentDatesById(id, newStartAt, newEndAt);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(tournament));
    }

    @PatchMapping("/name/{tournamentName}/dates")
    public ResponseEntity<TournamentResponse> updateTournamentDatesByTournamentName(@PathVariable String
                                                                                        tournamentName,
                                                                                    @RequestBody @Valid
                                                                                    DateRequest dateRequest) {
        Date newStartAt = dateRequest.getStartAt();
        Date newEndAt = dateRequest.getEndAt();
        Tournament updatedTournament =
            tournamentService.updateTournamentDatesByTournamentName(tournamentName, newStartAt, newEndAt);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/{id}/match-duration")
    public ResponseEntity<TournamentResponse> updateTournamentMatchDurationById(
        @PathVariable Long id,
        @RequestParam(name = "new-match-duration")
        @NotNull @Min(1) @Max(23)
        Integer newMatchDuration) {
        Tournament updatedTournament =
            tournamentService.updateTournamentMatchDurationById(id, newMatchDuration);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/name/{tournamentName}/match-duration")
    public ResponseEntity<TournamentResponse> updateTournamentMatchDurationByTournamentName(
        @PathVariable String tournamentName,
        @RequestParam(name = "new-match-duration")
        @NotNull @Min(1) @Max(23)
        Integer newMatchDuration) {
        Tournament updatedTournament =
            tournamentService.updateTournamentMatchDurationByTournamentName(tournamentName, newMatchDuration);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/{id}/hours")
    public ResponseEntity<TournamentResponse> updateTournamentHoursById(
        @PathVariable Long id, @RequestBody @Valid HourRequest hourRequest) {
        Integer newStartHour = hourRequest.getStartHour();
        Integer newEndHour = hourRequest.getEndHour();
        Tournament updatedTournament =
            tournamentService.updateTournamentHoursById(id, newStartHour, newEndHour);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/name/{tournamentName}/hours")
    public ResponseEntity<TournamentResponse> updateTournamentHoursByTournamentName(
        @PathVariable String tournamentName, @RequestBody @Valid HourRequest hourRequest) {
        Integer newStartHour = hourRequest.getStartHour();
        Integer newEndHour = hourRequest.getEndHour();
        Tournament updatedTournament =
            tournamentService.updateTournamentHoursByTournamentName(tournamentName, newStartHour, newEndHour);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/{id}/team-count")
    public ResponseEntity<TournamentResponse> updateTournamentTeamCountById(
        @PathVariable Long id,
        @RequestParam(name = "new-team-count")
        @NotNull @Min(2)
        Integer newTeamCount) {
        Tournament updatedTournament = tournamentService.updateTournamentTeamCountById(id, newTeamCount);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PutMapping("/name/{tournamentName}/team-count")
    public ResponseEntity<TournamentResponse> updateTournamentTeamCountByTournamentName(
        @PathVariable String tournamentName,
        @RequestParam(name = "new-team-count")
        @NotNull @Min(2)
        Integer newTeamCount) {
        Tournament updatedTournament =
            tournamentService.updateTournamentTeamCountByTournamentName(tournamentName, newTeamCount);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PatchMapping("/{id}/team-member-count")
    public ResponseEntity<TournamentResponse> updateTournamentTeamMemberCountById(
        @PathVariable Long id,
        @RequestParam(name = "new-team-member-count")
        @NotNull @Min(1)
        Integer newTeamMemberCount) {
        Tournament updatedTournament = tournamentService.updateTournamentTeamMemberCountById(id, newTeamMemberCount);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }

    @PutMapping("/name/{tournamentName}/team-member-count")
    public ResponseEntity<TournamentResponse> updateTournamentTeamMemberCountByTournamentName(
        @PathVariable String tournamentName,
        @RequestParam(name = "new-team-member-count")
        @NotNull @Min(1)
        Integer newTeamMemberCount) {
        Tournament updatedTournament =
            tournamentService.updateTournamentTeamMemberCountByTournamentName(tournamentName, newTeamMemberCount);
        return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
    }
}
