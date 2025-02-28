package com.example.practiceproject.utils.loader;

import com.example.practiceproject.utils.DatabaseEncryptionUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.dbencryption")
public class EncryptionConfig {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @PostConstruct
    public void loadKey() {
        DatabaseEncryptionUtils.loadKey(key);
    }
}