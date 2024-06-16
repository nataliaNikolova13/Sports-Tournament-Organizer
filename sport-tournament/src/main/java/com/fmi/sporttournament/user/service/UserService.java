package com.fmi.sporttournament.user.service;

import com.fmi.sporttournament.participant.repository.ParticipantRepository;

import com.fmi.sporttournament.team.entity.Team;

import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;
import com.fmi.sporttournament.tournament_participant.repository.TournamentParticipantRepository;

import com.fmi.sporttournament.user.dto.request.ChangeRoleRequest;

import com.fmi.sporttournament.email.service.EmailService;

import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.entity.role.Role;
import com.fmi.sporttournament.user.repository.UserRepositoty;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoty userRepo;
    private final ParticipantRepository participantRepository;
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final EmailService emailService;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof User user) {
            String email = user.getEmail();
            return userRepo.findByEmail(email).orElse(null);
        }

        return null;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepo.findById(userId);
    }

    public Optional<User> getUserByEmail(String userEmail) {
        return userRepo.findByEmail(userEmail);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepo.getByRole(role);
    }

    private void validateUserNotParticipateInTournament(User user) {
        List<Team> teams = participantRepository.findTeamsByUser(user);
        for (Team team : teams) {
            if (!tournamentParticipantRepository.findTournamentsByTeamAndStatus(team, TournamentParticipantStatus.joined)
                .isEmpty()) {
                throw new IllegalStateException("The user participate in tournament and it can't be deleted");
            }
        }
    }

    public void removeUser(Long id) {
        Optional<User> user = userRepo.findById(id);
        user.ifPresent(this::validateUserNotParticipateInTournament);
        userRepo.deleteById(id);
    }

    public Optional<User> updateUserRole(Long userId, ChangeRoleRequest changeRoleRequest) {
        User currentUser = getCurrentUser();

        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Permission denied. Only admin users can change user roles.");
        }
        return userRepo.findById(userId)
            .map(user -> {
                user.setRole(changeRoleRequest.getRole());
                return userRepo.save(user);
            });
    }
}
