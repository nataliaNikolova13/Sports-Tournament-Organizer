package com.fmi.sporttournament.Dto.requests;

import com.fmi.sporttournament.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeRoleRequest {
    private Role role;
}
