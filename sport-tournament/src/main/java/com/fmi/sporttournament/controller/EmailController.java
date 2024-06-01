package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.requests.EmailRequestAllUsers;
import com.fmi.sporttournament.Dto.requests.EmailRequestOneUser;
import com.fmi.sporttournament.Dto.responses.EmailResponse;
import com.fmi.sporttournament.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {
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
            System.out.println(e.getMessage());
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
