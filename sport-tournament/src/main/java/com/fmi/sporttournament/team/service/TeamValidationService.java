package com.fmi.sporttournament.team.service;

import com.fmi.sporttournament.exception.business.BusinessRuleViolationException;
import com.fmi.sporttournament.exception.business.OperationNotAllowedException;
import com.fmi.sporttournament.exception.resource.ResourceAlreadyExistsException;
import com.fmi.sporttournament.exception.resource.ResourceNotFoundException;

import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.team.entity.category.TeamCategory;
import com.fmi.sporttournament.team.repository.TeamRepository;

import com.fmi.sporttournament.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamValidationService {
    private final TeamRepository teamRepository;

    public Team validateTeamIdExist(Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()) {
            throw new ResourceNotFoundException("Team with this id " + id + " doesn't exist");
        }
        return team.get();
    }

    public Team validateTeamNameExist(String teamName) {
        Optional<Team> team = teamRepository.findByName(teamName);
        if (team.isEmpty()) {
            throw new ResourceNotFoundException("Team with name " + teamName + " doesn't exist");
        }
        return team.get();
    }

    public void validateTeamNameNotExist(String teamName) {
        if (teamRepository.findByName(teamName).isPresent()) {
            throw new ResourceAlreadyExistsException("Team with the name " + teamName + " already exists");
        }
    }

    public void validateUserAgeAndTeamCategory(User user, Team team) {
        if (user.getAge() > 18 && team.getCategory() == TeamCategory.youth) {
            throw new BusinessRuleViolationException(
                "The user " + user.getUsername() + " is over 18 year and he/she can't be added to the youth team " +
                    team.getName());
        }
    }
}
