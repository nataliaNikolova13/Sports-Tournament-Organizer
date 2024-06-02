package com.fmi.sporttournament.email.service;

import com.fmi.sporttournament.email.dto.request.EmailRequestAllUsers;
import com.fmi.sporttournament.email.dto.request.EmailRequestOneUser;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.repository.UserRepositoty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    private final UserRepositoty userRepository;

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
}