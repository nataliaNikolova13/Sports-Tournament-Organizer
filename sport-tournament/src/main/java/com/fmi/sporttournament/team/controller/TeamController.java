package com.fmi.sporttournament.team.controller;

import com.fmi.sporttournament.team.dto.request.TeamRegistrationRequest;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody TeamRegistrationRequest teamRegistrationRequest) {
        try {
            Team team = teamService.createTeam(teamRegistrationRequest);
            return ResponseEntity.ok(team);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable Long id) {
        try {
            teamService.deleteTeamById(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/name/{teamName}")
    public ResponseEntity<Void> deleteTournamentByTournamentName(@PathVariable String teamName) {
        try {
            teamService.deleteTeamByTournamentName(teamName);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
