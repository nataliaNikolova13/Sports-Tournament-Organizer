package com.fmi.sporttournament.tournament.controller;

import com.fmi.sporttournament.tournament.dto.request.DateRequest;
import com.fmi.sporttournament.tournament.dto.request.HourRequest;
import com.fmi.sporttournament.tournament.dto.request.TournamentRegistrationRequest;

import com.fmi.sporttournament.tournament.dto.response.TournamentResponse;

import com.fmi.sporttournament.tournament.entity.Tournament;

import com.fmi.sporttournament.tournament.mapper.TournamentMapper;

import com.fmi.sporttournament.tournament.service.TournamentService;

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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournament")
public class TournamentController {
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponse> getTournamentById(@PathVariable Long id) {
        Optional<Tournament> tournament = tournamentService.getTournamentById(id);
        return tournament.map(
                value -> new ResponseEntity<>(tournamentMapper.tournamentToResponse(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/name/{tournamentName}")
    public ResponseEntity<TournamentResponse> getTournamentByTournamentName(@PathVariable String tournamentName) {
        Optional<Tournament> tournamentOptional = tournamentService.getTournamentByTournamentName(tournamentName);
        return tournamentOptional.map(
                tournament -> new ResponseEntity<>(tournamentMapper.tournamentToResponse(tournament), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/location/{locationName}")
    public ResponseEntity<List<TournamentResponse>> getTournamentsByLocationName(@PathVariable String locationName) {
        try {
            List<Tournament> tournaments = tournamentService.getTournamentByLocationName(locationName);
            return ResponseEntity.ok(tournamentMapper.tournamentsToResponse(tournaments));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (
            Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sport/{sportType}")
    public ResponseEntity<List<TournamentResponse>> getTournamentsBySportType(@PathVariable String sportType) {
        try {
            List<Tournament> tournaments = tournamentService.getTournamentBySportType(sportType);
            return ResponseEntity.ok(tournamentMapper.tournamentsToResponse(tournaments));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (
            Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(
        @RequestBody TournamentRegistrationRequest tournamentRequest) {
        try {
            Tournament tournament = tournamentService.createTournament(tournamentRequest);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(tournament));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/start-tournament/{id}")
    public ResponseEntity<TournamentResponse> startTournament(@PathVariable Long id) {
        try {
            Tournament tournament = tournamentService.startTournamentById(id);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(tournament));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournamentById(@PathVariable Long id) {
        try {
            tournamentService.deleteTournamentById(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/name/{tournamentName}")
    public ResponseEntity<Void> deleteTournamentByTournamentName(@PathVariable String tournamentName) {
        try {
            tournamentService.deleteTournamentByTournamentName(tournamentName);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponse> updateTournament(@PathVariable Long id,
                                                               @RequestBody
                                                               TournamentRegistrationRequest tournamentRegistrationRequest) {
        try {
            Tournament updatedTournament = tournamentService.updateTournament(id, tournamentRegistrationRequest);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TournamentResponse> updateTournamentNameById(@PathVariable Long id,
                                                                       @RequestParam(name = "new-tournament-name")
                                                                       String newTournamentName) {
        try {
            Tournament updatedTournament =
                tournamentService.updateTournamentNameById(id, newTournamentName);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/name/{currentTournamentName}")
    public ResponseEntity<TournamentResponse> updateTournamentNameByTournamentName(
        @PathVariable String currentTournamentName,
        @RequestParam(name = "new-tournament-name")
        String newTournamentName) {
        try {
            Tournament updatedTournament =
                tournamentService.updateTournamentNameByTournamentName(currentTournamentName, newTournamentName);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<TournamentResponse> updateTournamentLocationByTId(@PathVariable Long id,
                                                                            @RequestParam(name = "new-tournament-location")
                                                                            String newLocationName) {
        try {
            Tournament updatedTournament =
                tournamentService.updateTournamentLocationById(id, newLocationName);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/name/{tournamentName}/location")
    public ResponseEntity<TournamentResponse> updateTournamentLocationByTournamentName(
        @PathVariable String tournamentName, @RequestParam(name = "new-tournament-location") String newLocationName) {
        try {
            Tournament updatedTournament =
                tournamentService.updateTournamentLocationByTournamentName(tournamentName, newLocationName);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/sport")
    public ResponseEntity<TournamentResponse> updateTournamentSportTypeById(@PathVariable Long id,
                                                                            @RequestParam(name = "new-sport-type")
                                                                            String newSportType) {
        try {
            Tournament updatedTournament = tournamentService.updateTournamentSportTypeById(id, newSportType);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/name/{tournamentName}/sport")
    public ResponseEntity<TournamentResponse> updateTournamentSportTypeByTournamentName(
        @PathVariable String tournamentName,
        @RequestParam(name = "new-sport-type") String newSportType
    ) {
        try {
            Tournament updatedTournament =
                tournamentService.updateTournamentSportTypeByTournamentName(tournamentName, newSportType);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/dates")
    public ResponseEntity<TournamentResponse> updateTournamentDatesById(@PathVariable Long id,
                                                                        @RequestBody DateRequest dateRequest) {
        try {
            Date newStartAt = dateRequest.getStartAt();
            Date newEndAt = dateRequest.getEndAt();
            Tournament tournament =
                tournamentService.updateTournamentDatesById(id, newStartAt, newEndAt);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(tournament));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/name/{tournamentName}/dates")
    public ResponseEntity<TournamentResponse> updateTournamentDatesByTournamentName(@PathVariable String tournamentName,
                                                                                    @RequestBody
                                                                                    DateRequest dateRequest) {
        try {
            Date newStartAt = dateRequest.getStartAt();
            Date newEndAt = dateRequest.getEndAt();
            Tournament updatedTournament =
                tournamentService.updateTournamentDatesByTournamentName(tournamentName, newStartAt, newEndAt);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/match-duration")
    public ResponseEntity<TournamentResponse> updateTournamentMatchDurationById(
        @PathVariable Long id, @RequestParam(name = "new-match-duration") Integer newMatchDuration) {
        try {
            Tournament updatedTournament = tournamentService.updateTournamentMatchDurationById(id, newMatchDuration);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/name/{tournamentName}/match-duration")
    public ResponseEntity<TournamentResponse> updateTournamentMatchDurationByTournamentName(
        @PathVariable String tournamentName, @RequestParam(name = "new-match-duration") Integer newMatchDuration) {
        try {
            Tournament updatedTournament =
                tournamentService.updateTournamentMatchDurationByTournamentName(tournamentName, newMatchDuration);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/hours")
    public ResponseEntity<TournamentResponse> updateTournamentHoursById(
        @PathVariable Long id, @RequestBody HourRequest hourRequest) {
        try {
            Integer newStartHour = hourRequest.getStartHour();
            Integer newEndHour = hourRequest.getEndHour();
            Tournament updatedTournament = tournamentService.updateTournamentHoursById(id, newStartHour, newEndHour);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/name/{tournamentName}/hours")
    public ResponseEntity<TournamentResponse> updateTournamentHoursByTournamentName(
        @PathVariable String tournamentName, @RequestBody HourRequest hourRequest) {
        try {
            Integer newStartHour = hourRequest.getStartHour();
            Integer newEndHour = hourRequest.getEndHour();
            Tournament updatedTournament =
                tournamentService.updateTournamentHoursByTournamentName(tournamentName, newStartHour, newEndHour);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/team-count")
    public ResponseEntity<TournamentResponse> updateTournamentTeamCountById(
        @PathVariable Long id, @RequestParam(name = "new-team-count") Integer newTeamCount) {
        try {
            Tournament updatedTournament = tournamentService.updateTournamentTeamCountById(id, newTeamCount);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/name/{tournamentName}/team-count")
    public ResponseEntity<TournamentResponse> updateTournamentTeamCountByTournamentName(
        @PathVariable String tournamentName, @RequestParam(name = "new-team-count") Integer newTeamCount) {
        try {
            Tournament updatedTournament =
                tournamentService.updateTournamentTeamCountByTournamentName(tournamentName, newTeamCount);
            return ResponseEntity.ok(tournamentMapper.tournamentToResponse(updatedTournament));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/categories")
    public List<String> getTournamentCategories() {
        return tournamentService.getTournamentCategories();
    }

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }
}
