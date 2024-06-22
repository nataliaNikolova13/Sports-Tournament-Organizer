package com.fmi.sporttournament.user.entity.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fmi.sporttournament.exception.business.OperationNotAllowedException;
import com.fmi.sporttournament.team.entity.category.TeamCategory;

public enum Role {
    Participant,
    Organizer,
    Admin;

    @JsonValue
    public String toValue() {
        return name();
    }

    @JsonCreator
    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new OperationNotAllowedException("Invalid value for Role: " + value);
    }
}
