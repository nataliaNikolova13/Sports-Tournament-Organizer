package com.fmi.sporttournament.user.authentication.dto.register.request;

import com.fmi.sporttournament.user.entity.role.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    @NotNull
    @NotBlank
    private String fullName;

    @NotNull
    @NotBlank
    @Pattern(regexp = ".+@.+\\..+", message = "Invalid email address")
    private String email;

    @NotNull
    @Past
    private LocalDate birthdate;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    private Role role;
}
