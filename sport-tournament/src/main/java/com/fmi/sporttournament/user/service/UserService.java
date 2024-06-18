package com.fmi.sporttournament.user.service;

import com.fmi.sporttournament.exception.business.OperationNotAllowedException;

import com.fmi.sporttournament.participant.repository.ParticipantRepository;

import com.fmi.sporttournament.team.entity.Team;

import com.fmi.sporttournament.email.service.EmailService;

import com.fmi.sporttournament.tournament_participant.entity.status.TournamentParticipantStatus;
import com.fmi.sporttournament.tournament_participant.repository.TournamentParticipantRepository;

import com.fmi.sporttournament.user.dto.request.ChangeRoleRequest;
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
    private final UserRepositoty userRepositoty;

    private final ParticipantRepository participantRepository;

    private final TournamentParticipantRepository tournamentParticipantRepository;

    private final EmailService emailService;

    private final UserValidationService userValidationService;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof User user) {
            String email = user.getEmail();
            return userRepositoty.findByEmail(email).orElse(null);
        }

        return null;
    }

    public List<User> getAllUsers() {
        return userRepositoty.findAll();
    }

    public User getUserById(Long userId) {
        return userValidationService.validateUserIdExist(userId);
    }

    public User getUserByEmail(String userEmail) {
        return userValidationService.validateUserEmailExist(userEmail);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepositoty.getByRole(role);
    }

    private void validateUserNotParticipateInTournament(User user) {
        List<Team> teams = participantRepository.findTeamsByUser(user);
        for (Team team : teams) {
            if (!tournamentParticipantRepository.findTournamentsByTeamAndStatus(team,
                    TournamentParticipantStatus.joined)
                .isEmpty()) {
                throw new OperationNotAllowedException("The user with username " + user.getUsername() +
                    " participate in tournament and it can't be deleted");
            }
        }
    }

    public void removeUser(Long id) {
        User user = userValidationService.validateUserIdExist(id);
        userRepositoty.deleteById(id);
    }

    public Optional<User> updateUserRole(Long userId, ChangeRoleRequest changeRoleRequest) {
        User currentUser = getCurrentUser();

        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Permission denied. Only admin users can change user roles.");
        }
        return userRepositoty.findById(userId)
            .map(user -> {
                user.setRole(changeRoleRequest.getRole());
                return userRepositoty.save(user);
            });
    }
}
