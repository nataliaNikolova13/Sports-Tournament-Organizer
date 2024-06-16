package com.fmi.sporttournament.export.match;

import com.fmi.sporttournament.email.dto.request.EmailRequestAllUsers;
import com.fmi.sporttournament.email.service.EmailService;
import com.fmi.sporttournament.match.entity.Match;
import com.fmi.sporttournament.match.repository.MatchRepository;
import com.fmi.sporttournament.match_result.entity.MatchResult;
import com.fmi.sporttournament.match_result.repository.MatchResultRepository;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.tournament.entity.Tournament;
import com.opencsv.CSVWriter;
import jakarta.mail.MessagingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class MatchInformation {
    private final MatchRepository matchRepository;
    private final MatchResultRepository matchResultRepository;
    private final EmailService emailService;

    private MatchResult validateMatchResultByMatchId(Long id) {
        Optional<MatchResult> matchResult = matchResultRepository.findByMatchId(id);
        if (matchResult.isEmpty()) {
            throw new IllegalArgumentException("The match result haven't been recorded");
        }
        return matchResult.get();
    }

    private void exportTournamentMatchesToCSV(Tournament tournament, String filePath) throws IOException {
        List<Match> matches = matchRepository.findAllByTournamentOrderByRoundNumber(tournament);

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            String[] header =
                {"Match ID", "Tournament", "Round Number", "Team 1", "Team 2", "Team 1 Score", "Team 2 Score", "Winner",
                    "Match Date","Venue"};
            writer.writeNext(header);

            for (Match match : matches) {
                MatchResult matchResult = validateMatchResultByMatchId(match.getId());
                String[] data = {
                    match.getId().toString(),
                    tournament.getTournamentName(),
                    match.getRound().getRoundNumber().toString(),
                    match.getTeam1().getName(),
                    match.getTeam2().getName(),
                    matchResult.getScoreTeam1().toString(),
                    matchResult.getScoreTeam2().toString(),
                    matchResult.getWinningTeam().getName(),
                    match.getMatchTime().toString(),
                    match.getVenue().toString()
                };
                writer.writeNext(data);
            }
        }
    }

    public void sendEmailWithCsvAttachment(Team team, EmailRequestAllUsers emailRequestAllUsers, String tempFilePath,
                                           Tournament tournament) {
        File tempFile = new File(tempFilePath);
        try {
            exportTournamentMatchesToCSV(tournament, tempFilePath);
            emailService.sendEmailToUserInTeamWithAttachment(team, emailRequestAllUsers, tempFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tempFile.exists()) {
                if (tempFile.delete()) {
                    System.out.println("Temporary file deleted successfully.");
                } else {
                    System.out.println("Failed to delete the temporary file.");
                }
            }
        }
    }
}
