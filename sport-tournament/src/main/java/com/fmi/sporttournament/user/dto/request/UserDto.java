package com.fmi.sporttournament.user.dto.request;

import com.fmi.sporttournament.user.entity.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
}
