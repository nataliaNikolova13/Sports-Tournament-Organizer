package com.fmi.sporttournament.user.authentication.dto.register.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String email;

    private String fullName;

    private String token;

    private long expiresIn;
}
