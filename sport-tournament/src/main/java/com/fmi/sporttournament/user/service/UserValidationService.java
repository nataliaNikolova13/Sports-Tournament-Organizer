package com.fmi.sporttournament.user.service;

import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.repository.UserRepositoty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final UserRepositoty userRepositoty;

    public User validateUserIdExist(Long id) {
        Optional<User> user = userRepositoty.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("The user with id " + id + " doesn't exist");
        }
        return user.get();
    }

    public User validateUserEmailExist(String email) {
        Optional<User> user = userRepositoty.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("The user with email " + email + " doesn't exist");
        }
        return user.get();
    }
}
