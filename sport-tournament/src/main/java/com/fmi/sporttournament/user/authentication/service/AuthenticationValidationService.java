package com.fmi.sporttournament.user.authentication.service;

import com.fmi.sporttournament.exception.business.BusinessRuleViolationException;
import com.fmi.sporttournament.exception.resource.ResourceAlreadyExistsException;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.repository.UserRepositoty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationValidationService {
    private final UserRepositoty userRepository;
    public void validateUserAge(User user) {
        if (user.getAge() < 12) {
            throw new BusinessRuleViolationException("The user is underage (less than 12 years old) for the platform.");
        }
    }

    public void validateUsernameNotExist(String username) {
        if (userRepository.findByFullName(username).isPresent()) {
            throw new ResourceAlreadyExistsException("User with the username " + username + " already exists");
        }
    }

    public void validateEmailNotExist(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResourceAlreadyExistsException("User with the email " + email + " already exists");
        }
    }

    private boolean isStrongPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void validatePasswordStrongEnough(String password) {
        if (!isStrongPassword(password)) {
            throw new BusinessRuleViolationException("The password isn't strong enough");
        }
    }
}
