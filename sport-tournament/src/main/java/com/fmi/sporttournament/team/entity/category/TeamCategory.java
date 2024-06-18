package com.fmi.sporttournament.team.entity.category;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fmi.sporttournament.exception.business.OperationNotAllowedException;

public enum TeamCategory {
    amateur, professional, youth;

    @JsonValue
    public String toValue() {
        return name();
    }

    @JsonCreator
    public static TeamCategory fromValue(String value) {
        for (TeamCategory category : TeamCategory.values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new OperationNotAllowedException("Invalid value for TeamCategory: " + value);
    }
}
