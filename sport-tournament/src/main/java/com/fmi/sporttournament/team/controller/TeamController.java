package com.fmi.sporttournament.team.controller;

import com.fmi.sporttournament.match_result.entity.MatchResult;
import com.fmi.sporttournament.match_result.service.MatchResultService;
import com.fmi.sporttournament.team.dto.request.ChangeCategoryRequest;
import com.fmi.sporttournament.team.dto.request.TeamRequest;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.entity.category.TeamCategory;
import com.fmi.sporttournament.team.service.TeamService;
import com.fmi.sporttournament.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody TeamRequest teamRegistrationRequest) {
        try {
            Team team = teamService.createTeam(teamRegistrationRequest);
            return ResponseEntity.ok(team);
        } catch (IllegalArgumentException | IllegalStateException e) {
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
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/name/{teamName}")
    public ResponseEntity<Void> deleteTournamentByTournamentName(@PathVariable String teamName) {
        try {
            teamService.deleteTeamByTournamentName(teamName);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeamNameById(@PathVariable Long id, @RequestBody TeamRequest teamRequest) {
        try {
            Team updatedTeam = teamService.updateTeam(id, teamRequest);
            return ResponseEntity.ok(updatedTeam);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Team> updateTeamNameById(@PathVariable Long id,
                                                   @RequestParam(name = "new-team-name") String newTeamName) {
        try {
            Team updatedTeam = teamService.updateTeamNameById(id, newTeamName);
            return ResponseEntity.ok(updatedTeam);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/name/{currentTeamName}")
    public ResponseEntity<Team> updateTeamNameByTeamName(@RequestParam String currentTeamName,
                                                         @RequestParam(name = "new-team-name") String newTeamName) {
        try {
            Team updatedTeam = teamService.updateTeamNameByTeamName(currentTeamName, newTeamName);
            return ResponseEntity.ok(updatedTeam);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/category")
    public ResponseEntity<Team> updateCategoryById(@PathVariable Long id, @RequestBody
    ChangeCategoryRequest changeCategoryRequest) {
        try {
            TeamCategory teamCategory = changeCategoryRequest.getTeamCategory();
            Team updatedTeam = teamService.updateCategoryById(id, teamCategory);
            return ResponseEntity.ok(updatedTeam);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/name/{teamName}")
    public ResponseEntity<Team> updateCategoryByTeamName(@RequestParam String teamName,
                                                         @RequestBody ChangeCategoryRequest changeCategoryRequest) {
        try {
            TeamCategory teamCategory = changeCategoryRequest.getTeamCategory();
            Team updatedTeam = teamService.updateCategoryByTeamName(teamName, teamCategory);
            return ResponseEntity.ok(updatedTeam);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/my-teams")
    public List<Team> getTeamsForCurrentUser(@AuthenticationPrincipal User user) {
        return teamService.getTeamsForUser(user);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long teamId) {
        Optional<Team> team = teamService.getTeamById(teamId);
        return team.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
