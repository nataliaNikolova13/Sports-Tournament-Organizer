package com.fmi.sporttournament.services;

import com.fmi.sporttournament.entity.Tournament;

import com.fmi.sporttournament.repositories.TournamentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentRepository.findById(id);
    }

    public Optional<Tournament> getTournamentByTournamentName(String tournamentName) {
        return tournamentRepository.findByTournamentName(tournamentName);
    }

    public List<Tournament> getTournamentBySportType(String sportType) {
        return tournamentRepository.findBySportType(sportType);
    }

    public List<Tournament> getTournamentByLocationName(String locationName) {
        return tournamentRepository.findByLocationLocationName(locationName);
    }

    public void removeTournament(Long id) {
        tournamentRepository.deleteById(id);
    }
}
