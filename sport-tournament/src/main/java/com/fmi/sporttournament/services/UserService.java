package com.fmi.sporttournament.services;

import com.fmi.sporttournament.Dto.requests.ChangeRoleRequest;
import com.fmi.sporttournament.entity.User;
import com.fmi.sporttournament.entity.enums.Role;
import com.fmi.sporttournament.repositories.UserRepositoty;
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

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long userId){
        return userRepo.findById(userId);
    }

    public Optional<User> getUserByEmail(String userEmail){
        return userRepo.findByEmail(userEmail);
    }

    public List<User> getUsersByRole(Role role){
        return userRepo.getByRole(role);
    }

    public void removeUser(Long id){
        userRepo.deleteById(id);
    }

    public Optional<User> updateUserRole(Long userId, ChangeRoleRequest changeRoleRequest){
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
