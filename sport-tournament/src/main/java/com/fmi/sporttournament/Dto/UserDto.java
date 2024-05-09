package com.fmi.sporttournament.Dto;

import com.fmi.sporttournament.entity.enums.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDto {
    private String fullName;
    private String email;
    private Role role;
}
