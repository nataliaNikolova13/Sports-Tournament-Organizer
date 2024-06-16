package com.fmi.sporttournament.user.authentication.service;

import com.fmi.sporttournament.user.authentication.dto.login.request.LoginUserDto;
import com.fmi.sporttournament.user.authentication.dto.register.request.RegisterUserDto;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.repository.UserRepositoty;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {
    private final UserRepositoty userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private void validateUserAge(User user) {
        if (user.getAge() < 12) {
            throw new IllegalArgumentException("The user is underage (less than 12 years old) for the platform.");
        }
    }

    private void validateUsernameNotExist(String username) {
        if (userRepository.findByFullName(username).isPresent()) {
            throw new IllegalArgumentException("User with the username " + username + " already exists");
        }
    }

    private void validateEmailNotExist(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with the email " + email + " already exists");
        }
    }

    private void validateUsernameIsNotBlank(String username) {
        if (username.isBlank()) {
            throw new IllegalArgumentException("Username name can't be blank");
        }
    }

    private void validateEmailIsNotBlank(String email) {
        if (email.isBlank()) {
            throw new IllegalArgumentException("Email name can't be blank");
        }
    }

    private boolean isStrongPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void validatePasswordStrongEnough(String password) {
        if (!isStrongPassword(password)) {
            throw new IllegalArgumentException("The password isn't strong enough");
        }
    }

    public AuthenticationService(
        UserRepositoty userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        String fullName = input.getFullName();
        String email = input.getEmail();
        String password = input.getPassword();

        validateUsernameIsNotBlank(fullName);
        validateUsernameNotExist(fullName);
        validateEmailIsNotBlank(email);
        validateEmailNotExist(email);
        validatePasswordStrongEnough(password);

        var user = User.builder()
            .fullName(input.getFullName())
            .email(input.getEmail())
            .birthdate(input.getBirthdate())
            .password(passwordEncoder.encode(input.getPassword()))
            .role(input.getRole())
            .build();
        validateUserAge(user);
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
            )
        );

        return userRepository.findByEmail(input.getEmail())
            .orElseThrow();
    }
}
