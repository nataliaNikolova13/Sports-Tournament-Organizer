package com.fmi.sporttournament.email.emails.tournament;

import com.fmi.sporttournament.email.dto.request.EmailRequestAllUsers;
import com.fmi.sporttournament.email.service.EmailService;
import com.fmi.sporttournament.team.entity.Team;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class TournamentEnrollmentEmail {
    private final EmailService emailService;

    public void sendTournamentEnrollmentEmail(Team team, String tournamentName) {
        EmailRequestAllUsers emailRequestAllUsers = new EmailRequestAllUsers("Tournament enrollment",
            "Team " + team.getName() + " has been automatically enrolled to the tournament " + tournamentName +
                " after another team has left.");
        emailService.sendEmailToAllUsersInTeam(team, emailRequestAllUsers);
    }
}
