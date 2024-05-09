package com.fmi.sporttournament.Dto.requests;

import com.fmi.sporttournament.entity.enums.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ChangeRoleRequest {
    private Role role;
}
