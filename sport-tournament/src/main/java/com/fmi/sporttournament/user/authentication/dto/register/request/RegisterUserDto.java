package com.fmi.sporttournament.user.authentication.dto.register.request;

import com.fmi.sporttournament.user.entity.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    private String fullName;
    private String email;
    private String password;
    private Role role;
}
