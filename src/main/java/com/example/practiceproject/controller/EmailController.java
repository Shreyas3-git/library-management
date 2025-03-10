package com.example.practiceproject.controller;

import com.example.practiceproject.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/simple")
    public ResponseEntity<String> sendSimpleEmail(
            @RequestBody EmailService.EmailRequest request
    ) {
        emailService.sendSimpleEmail(request);
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/attachment")
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestBody EmailService.EmailRequest request
    ) throws MessagingException {
        emailService.sendEmailWithAttachment(request);
        return ResponseEntity.ok("Email with attachment sent");
    }
}