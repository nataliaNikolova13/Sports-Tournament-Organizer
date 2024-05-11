package com.fmi.sporttournament.Dto;

import com.fmi.sporttournament.entity.enums.Role;
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
