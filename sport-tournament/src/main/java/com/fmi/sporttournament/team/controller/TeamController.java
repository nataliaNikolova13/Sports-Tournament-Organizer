package com.fmi.sporttournament.team.controller;

import com.fmi.sporttournament.team.dto.request.TeamRequest;
import com.fmi.sporttournament.team.dto.response.TeamResponse;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.entity.category.TeamCategory;
import com.fmi.sporttournament.team.mapper.TeamMapper;
import com.fmi.sporttournament.team.service.TeamService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;
    private final TeamMapper teamMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Long id) {
        Team team = teamService.getTeamById(id);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        List<Team> team = teamService.getAllTeams();
        return ResponseEntity.ok(teamMapper.teamsToResponse(team));
    }

    @GetMapping("/all-active-teams")
    public ResponseEntity<List<TeamResponse>> getAllActiveTeams() {
        List<Team> team = teamService.getAllActiveTeams();
        return ResponseEntity.ok(teamMapper.teamsToResponse(team));
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid TeamRequest teamRegistrationRequest) {
        Team team = teamService.createTeam(teamRegistrationRequest);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable Long id) {
        teamService.deleteTeamById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/name/{teamName}")
    public ResponseEntity<Void> deleteTournamentByTournamentName(@PathVariable String teamName) {
        teamService.deleteTeamByTournamentName(teamName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeamNameById(@PathVariable Long id,
                                                           @RequestBody @Valid TeamRequest teamRequest) {
        Team team = teamService.updateTeam(id, teamRequest);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeamNameById(@PathVariable Long id,
                                                           @RequestParam(name = "new-team-name")
                                                           @NotNull @NotBlank String newTeamName) {
        Team team = teamService.updateTeamNameById(id, newTeamName);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @PatchMapping("/name/{currentTeamName}")
    public ResponseEntity<TeamResponse> updateTeamNameByTeamName(@RequestParam String currentTeamName,
                                                                 @RequestParam(name = "new-team-name")
                                                                 @NotNull @NotBlank String newTeamName) {
        Team team = teamService.updateTeamNameByTeamName(currentTeamName, newTeamName);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @PatchMapping("/{id}/category")
    public ResponseEntity<TeamResponse> updateCategoryById(@PathVariable Long id,
                                                           @RequestParam(name = "new-category")
                                                           TeamCategory teamCategory) {
        Team team = teamService.updateCategoryById(id, teamCategory);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @PatchMapping("/name/{teamName}")
    public ResponseEntity<TeamResponse> updateCategoryByTeamName(@RequestParam String teamName,
                                                                 @RequestParam(name = "new-category")
                                                                 TeamCategory teamCategory) {
        Team team = teamService.updateCategoryByTeamName(teamName, teamCategory);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }
}
