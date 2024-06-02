package com.fmi.sporttournament.email.controller;

import com.fmi.sporttournament.email.dto.request.EmailRequestAllUsers;
import com.fmi.sporttournament.email.dto.request.EmailRequestOneUser;
import com.fmi.sporttournament.email.dto.response.EmailResponse;
import com.fmi.sporttournament.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<EmailResponse> sendEmailToUserByEmail(@RequestBody
                                                                EmailRequestOneUser email) {
        try {
            String to = email.getTo();
            String subject = email.getSubject();
            String text = email.getText();
            emailService.sendEmailToUserByEmail(email);
            return ResponseEntity.ok(new EmailResponse(to, subject, text));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/send-email-to-all")
    public ResponseEntity<Void> sendEmailToAllUsers(@RequestBody EmailRequestAllUsers email) {
        try {
            emailService.sendEmailToAllUsers(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
