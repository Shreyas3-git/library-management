package com.example.practiceproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig
{
    @Value("${app.base-url}")
    private String baseUrl;
    @Value("${app.user.create-endpoint}")
    private String createUser;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getCreateUser() {
        return createUser;
    }
}
