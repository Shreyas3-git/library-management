package com.example.practiceproject.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final Environment environment;

    // Simple text email
    public void sendSimpleEmail(EmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(environment.getProperty("spring.mail.username"));
        message.setTo(request.to());
        message.setSubject(request.subject());
        message.setText(request.body());

        try {
            mailSender.send(message);
            log.info("Email sent to {}", request.to());
        } catch (MailException ex) {
            log.error("Failed to send email: {}", ex.getMessage());
            throw new EmailException("Email sending failed");
        }
    }

    // Email with attachments
    public void sendEmailWithAttachment(EmailRequest request)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(environment.getProperty("spring.mail.username"));
        helper.setTo(request.to());
        helper.setSubject(request.subject());
        helper.setText(request.body());

        // Add attachment
        FileSystemResource file = new FileSystemResource(
                new File(request.attachmentPath())
        );
        helper.addAttachment("Attachment.pdf", file);

        mailSender.send(message);
    }

    // HTML email with template
    public void sendHtmlEmail(EmailRequest request)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(environment.getProperty("spring.mail.username"));
        helper.setTo(request.to());
        helper.setSubject(request.subject());

        // HTML content with inline image
        String content = "<html><body>"
                + "<h1>" + request.body() + "</h1>"
                + "<img src='cid:logo'>"
                + "</body></html>";

        helper.setText(content, true);

        // Add inline image
        ClassPathResource resource = new ClassPathResource("static/logo.png");
        helper.addInline("logo", resource);

        mailSender.send(message);
    }

    // Record for email requests
    public record EmailRequest(
            String to,
            String subject,
            String body,
            String attachmentPath
    ) {}

    // Custom exception
    public static class EmailException extends RuntimeException {
        public EmailException(String message) {
            super(message);
        }
    }
}