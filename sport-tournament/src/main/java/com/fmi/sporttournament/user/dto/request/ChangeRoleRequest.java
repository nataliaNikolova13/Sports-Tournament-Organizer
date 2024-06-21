package com.fmi.sporttournament.user.dto.request;

import com.fmi.sporttournament.user.entity.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleRequest {
    private Role role;
}
