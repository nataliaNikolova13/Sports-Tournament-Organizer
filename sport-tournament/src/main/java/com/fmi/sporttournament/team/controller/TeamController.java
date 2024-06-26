package com.fmi.sporttournament.team.controller;

import com.fmi.sporttournament.match_result.entity.MatchResult;
import com.fmi.sporttournament.match_result.service.MatchResultService;
//import com.fmi.sporttournament.team.dto.request.ChangeCategoryRequest;
import com.fmi.sporttournament.team.dto.request.TeamRequest;
import com.fmi.sporttournament.team.dto.response.TeamResponse;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.entity.category.TeamCategory;
import com.fmi.sporttournament.team.mapper.TeamMapper;
import com.fmi.sporttournament.team.service.TeamService;
import com.fmi.sporttournament.user.entity.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Long id) {
        Team team = teamService.getTeamById(id);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        List<Team> team = teamService.getAllTeams();
        return ResponseEntity.ok(teamMapper.teamsToResponse(team));
    }

    @GetMapping("/all-active-teams")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<List<TeamResponse>> getAllActiveTeams() {
        List<Team> team = teamService.getAllActiveTeams();
        return ResponseEntity.ok(teamMapper.teamsToResponse(team));
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid TeamRequest teamRegistrationRequest) {
        Team team = teamService.createTeam(teamRegistrationRequest);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<Void> deleteTeamById(@PathVariable Long id) {
        teamService.deleteTeamById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/name/{teamName}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<Void> deleteTournamentByTournamentName(@PathVariable String teamName) {
        teamService.deleteTeamByTournamentName(teamName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<TeamResponse> updateTeamNameById(@PathVariable Long id,
                                                           @RequestBody @Valid TeamRequest teamRequest) {
        Team team = teamService.updateTeam(id, teamRequest);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<TeamResponse> updateTeamNameById(@PathVariable Long id,
                                                           @RequestParam(name = "new-team-name")
                                                           @NotNull @NotBlank String newTeamName) {
        Team team = teamService.updateTeamNameById(id, newTeamName);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @PatchMapping("/name/{currentTeamName}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<TeamResponse> updateTeamNameByTeamName(@RequestParam String currentTeamName,
                                                                 @RequestParam(name = "new-team-name")
                                                                 @NotNull @NotBlank String newTeamName) {
        Team team = teamService.updateTeamNameByTeamName(currentTeamName, newTeamName);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @PatchMapping("/{id}/category")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<TeamResponse> updateCategoryById(@PathVariable Long id,
                                                           @RequestParam(name = "new-category")
                                                           TeamCategory teamCategory) {
        Team team = teamService.updateCategoryById(id, teamCategory);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @PatchMapping("/name/{teamName}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<TeamResponse> updateCategoryByTeamName(@RequestParam String teamName,
                                                                 @RequestParam(name = "new-category")
                                                                 TeamCategory teamCategory) {
        Team team = teamService.updateCategoryByTeamName(teamName, teamCategory);
        return ResponseEntity.ok(teamMapper.teamToResponse(team));
    }

    @GetMapping("/my-teams")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public List<Team> getTeamsForCurrentUser(@AuthenticationPrincipal User user) {
        return teamService.getTeamsForUser(user);
    }

//    @GetMapping("/{teamId}")
//    public ResponseEntity<Team> getTeamById(@PathVariable Long teamId) {
//        Optional<Team> team = teamService.getTeamById(teamId);
//        return team.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
}
