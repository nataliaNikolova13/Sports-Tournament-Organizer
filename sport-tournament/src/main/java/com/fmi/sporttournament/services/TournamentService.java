package com.fmi.sporttournament.services;

import com.fmi.sporttournament.Dto.requests.ChangeRoleRequest;
import com.fmi.sporttournament.entity.Tournament;
import com.fmi.sporttournament.entity.User;
import com.fmi.sporttournament.entity.enums.Role;
import com.fmi.sporttournament.repositories.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> getTournamentById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId);
    }

    public Optional<Tournament> getTournamentByTournamentName(String tournamentName) {
        return tournamentRepository.findByTournamentName(tournamentName);
    }

    public List<Tournament> getTournamentBySportType(String sportType) {
        return tournamentRepository.findBySportType(sportType);
    }

    public List<Tournament> getTournamentByLocation(String locationName) {
        return tournamentRepository.findByLocation(locationName);
    }

    public void removeTournament(Long id) {
        tournamentRepository.deleteById(id);
    }
}
