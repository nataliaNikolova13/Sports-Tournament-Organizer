package com.fmi.sporttournament.Dto;

import com.fmi.sporttournament.entity.enums.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
}
