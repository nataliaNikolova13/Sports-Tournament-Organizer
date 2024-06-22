package com.fmi.sporttournament.tournament.entity.category;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fmi.sporttournament.exception.business.OperationNotAllowedException;

public enum TournamentCategory {
    amateur, professional, youth;

    @JsonValue
    public String toValue() {
        return name();
    }

    @JsonCreator
    public static TournamentCategory fromValue(String value) {
        for (TournamentCategory category : TournamentCategory.values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new OperationNotAllowedException("Invalid value for TournamentCategory: " + value);
    }
}
