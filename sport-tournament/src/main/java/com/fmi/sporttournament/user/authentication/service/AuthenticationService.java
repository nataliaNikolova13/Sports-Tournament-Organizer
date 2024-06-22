package com.fmi.sporttournament.user.authentication.service;

import com.fmi.sporttournament.user.authentication.dto.login.request.LoginUserDto;
import com.fmi.sporttournament.user.authentication.dto.register.request.RegisterUserDto;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.repository.UserRepositoty;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepositoty userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final AuthenticationValidationService authenticationValidationService;

    public User signup(RegisterUserDto input) {
        String fullName = input.getFullName();
        String email = input.getEmail();
        String password = input.getPassword();

        authenticationValidationService.validateUsernameNotExist(fullName);
        authenticationValidationService.validateEmailNotExist(email);
        authenticationValidationService.validatePasswordStrongEnough(password);

        var user = User.builder()
            .fullName(input.getFullName())
            .email(input.getEmail())
            .birthdate(LocalDate.parse(input.getBirthdate()))
            .password(passwordEncoder.encode(input.getPassword()))
            .role(input.getRole())
            .build();
        authenticationValidationService.validateUserAge(user);
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
