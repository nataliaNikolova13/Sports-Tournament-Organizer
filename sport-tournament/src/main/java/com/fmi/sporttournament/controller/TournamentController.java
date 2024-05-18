package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.TournamentDto;
import com.fmi.sporttournament.Dto.UserDto;
import com.fmi.sporttournament.Dto.requests.ChangeRoleRequest;
import com.fmi.sporttournament.entity.Location;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.User;
import com.fmi.sporttournament.entity.enums.Role;
import com.fmi.sporttournament.mapper.TournamentMapper;
import com.fmi.sporttournament.mapper.UserMapper;
import com.fmi.sporttournament.services.TournamentService;
import com.fmi.sporttournament.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournament")
public class TournamentController {
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;

    @GetMapping
    public ResponseEntity<List<TournamentDto>> getAllTournaments() {
        List<Tournament> allTournament = tournamentService.getAllTournaments();
        return new ResponseEntity<>(tournamentMapper.tournamentsToTournamentDtos(allTournament), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentDto> getTournamentById(@PathVariable Long id) {
        Optional<Tournament> tournament = tournamentService.getTournamentById(id);
        return tournament.map(value -> new ResponseEntity<>(tournamentMapper.tournamentToDto(value), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/location/{locationName}")
    public ResponseEntity<List<TournamentDto>> getUserByLocation(@PathVariable String locationName) {
        List<Tournament> tournaments = tournamentService.getTournamentByLocation(locationName);
        return new ResponseEntity<>(tournamentMapper.tournamentsToTournamentDtos(tournaments), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        tournamentService.removeTournament(id);
        return ResponseEntity.ok().build();
    }

}
