package com.fmi.sporttournament.email.service;

import com.fmi.sporttournament.email.dto.request.EmailRequestAllUsers;
import com.fmi.sporttournament.email.dto.request.EmailRequestOneUser;
import com.fmi.sporttournament.participant.repository.ParticipantRepository;
import com.fmi.sporttournament.team.entity.Team;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.repository.UserRepositoty;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    private final UserRepositoty userRepository;
    private final ParticipantRepository participantRepository;

    @Value("${spring.mail.username}")
    private String from;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    private User validateUserEmailExist(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with email " + email + " doesn't exist");
        }
        return user.get();
    }

    public void sendEmailToUserByEmail(EmailRequestOneUser email) {
        String to = email.getTo();
        String subject = email.getSubject();
        String text = email.getText();
        User user = validateUserEmailExist(to);
        sendSimpleMessage(user.getEmail(), subject, text);
    }

    public void sendEmailToAllUsers(EmailRequestAllUsers email) {
        String subject = email.getSubject();
        String text = email.getText();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            sendSimpleMessage(user.getEmail(), subject, text);
        }
    }

    public void sendEmailWithAttachment(String to, String subject, String text, String pathToAttachment) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment(file.getFilename(), file);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmailToUserWithAttachment(EmailRequestOneUser email, String pathToAttachment) {
        String to = email.getTo();
        String subject = email.getSubject();
        String text = email.getText();
        User user = validateUserEmailExist(to);
        sendEmailWithAttachment(user.getEmail(), subject, text, pathToAttachment);
    }

    public void sendEmailToUserInTeamWithAttachment(Team team, EmailRequestAllUsers email, String pathToAttachment) {
        String subject = email.getSubject();
        String text = email.getText();

        List<User> users = participantRepository.findUsersByTeam(team);
        for (User user : users) {
            sendEmailWithAttachment(user.getEmail(), subject, text, pathToAttachment);
        }
    }

    public void sendEmailToAllUsersWithAttachment(EmailRequestAllUsers email, String pathToAttachment) {
        String subject = email.getSubject();
        String text = email.getText();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            sendEmailWithAttachment(user.getEmail(), subject, text, pathToAttachment);
        }
    }
}