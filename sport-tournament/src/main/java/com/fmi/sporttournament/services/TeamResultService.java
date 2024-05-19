package com.fmi.sporttournament.services;

import com.fmi.sporttournament.entity.TeamResult;

import com.fmi.sporttournament.repositories.TeamResultRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamResultService {
    private final TeamResultRepository teamResultRepository;

    public List<TeamResult> getAllMatches() {
        return teamResultRepository.findAll();
    }

    public Optional<TeamResult> getMatchById(Long id) {
        return teamResultRepository.findById(id);
    }

    public Optional<TeamResult> getMatchByTeamId(Long teamId) {
        return teamResultRepository.findByTeamId(teamId);
    }

    public List<TeamResult> getTeamResultByTournamentId(Long tournamentId) {
        return teamResultRepository.findByTournamentId(tournamentId);
    }

    public void removeTeamResult(Long id) {
        teamResultRepository.deleteById(id);
    }
}
