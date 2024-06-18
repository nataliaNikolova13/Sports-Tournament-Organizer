package com.fmi.sporttournament.user.dto.request;

import com.fmi.sporttournament.user.entity.role.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeRoleRequest {
    @NotNull
    private Role role;
}
