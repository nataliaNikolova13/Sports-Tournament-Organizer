package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.requests.TeamRegistrationRequest;
import com.fmi.sporttournament.Dto.responses.TeamResponse;
import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.enums.TeamCategory;
import com.fmi.sporttournament.mapper.TeamMapper;
import com.fmi.sporttournament.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<TeamCategory>> getTeamCategories() {
        List<TeamCategory> categories = teamService.getTeamCategories();
        return ResponseEntity.ok(categories);
    }

}
