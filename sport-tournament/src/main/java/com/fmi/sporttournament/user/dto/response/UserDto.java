package com.fmi.sporttournament.user.dto.response;

import com.fmi.sporttournament.user.entity.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private LocalDate birthdate;
    private String email;
    private Role role;
}
