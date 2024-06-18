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
public class TournamentCancellationEmail {

    private final EmailService emailService;

    public void sendTournamentCancellationEmail(Team team, String tournamentName) {
        EmailRequestAllUsers emailRequestAllUsers = new EmailRequestAllUsers("Tournament cancellation",
            "Tournament " + tournamentName +
                " has been cancelled. All teams have been removed from the tournament.");
        emailService.sendEmailToAllUsersInTeam(team, emailRequestAllUsers);
    }
}
