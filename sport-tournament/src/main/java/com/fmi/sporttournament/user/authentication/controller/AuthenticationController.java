package com.fmi.sporttournament.user.authentication.controller;

import com.fmi.sporttournament.user.authentication.dto.login.request.LoginUserDto;
import com.fmi.sporttournament.user.authentication.dto.register.request.RegisterUserDto;
import com.fmi.sporttournament.user.authentication.dto.login.response.LoginResponse;
import com.fmi.sporttournament.user.authentication.dto.register.response.RegisterResponse;

import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.authentication.service.AuthenticationService;
import com.fmi.sporttournament.user.authentication.jwt.service.JwtService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        String jwtToken = jwtService.generateToken(registeredUser);

        RegisterResponse registerResponse = RegisterResponse.builder()
            .fullName(registeredUser.getFullName())
            .email(registeredUser.getEmail())
            .token(jwtToken)
            .expiresIn(jwtService.getExpirationTime())
            .build();

        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
            .token(jwtToken)
            .expiresIn(jwtService.getExpirationTime())
            .build();

        return ResponseEntity.ok(loginResponse);
    }
}
