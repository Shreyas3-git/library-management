package com.example.practiceproject.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig
{

    @Value("${app.notifications.twilio.account-sid}")
    private String accountSid;
    @Value("${app.notifications.twilio.service-sid}")
    private String serviceSid;
    @Value("${app.notifications.twilio.auth-token}")
    private String authToken;

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.issuer-uri}")
    private String issuerUri;

    @PostConstruct
    public void logTwilioConfig() {
        System.out.println("Twilio Account SID: " + accountSid);
        System.out.println("Twilio Service SID: " + serviceSid);
        System.out.println("Twilio Auth Token: " + authToken);
        System.out.println("JWT Secret : "+jwtSecret);
        System.out.println("IssuerUrl : "+issuerUri);
    }
}
