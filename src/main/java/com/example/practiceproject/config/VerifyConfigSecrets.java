package com.example.practiceproject.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VerifyConfigSecrets
{

    @Value("${app.notifications.twilio.account-sid}")
    private String accountSid;
    @Value("${app.notifications.twilio.service-sid}")
    private String serviceSid;
    @Value("${app.notifications.twilio.auth-token}")
    private String authToken;

    @PostConstruct
    public void logTwilioConfig() {
        System.out.println("Twilio Account SID: " + accountSid);
        System.out.println("Twilio Service SID: " + serviceSid);
        System.out.println("Twilio Auth Token: " + authToken);
    }
}
