package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.requests.TeamRegistrationRequest;
import com.fmi.sporttournament.entity.Team;
import com.fmi.sporttournament.mapper.TeamMapper;
import com.fmi.sporttournament.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        Team team = teamService.createTeam(teamRegistrationRequest);
        return ResponseEntity.ok(team);
    }
}
