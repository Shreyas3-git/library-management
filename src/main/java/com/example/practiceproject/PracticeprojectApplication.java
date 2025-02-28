package com.example.practiceproject;

import com.example.practiceproject.utils.loader.EncryptionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableConfigurationProperties(EncryptionConfig.class)
@EnableFeignClients
@EnableMethodSecurity
public class PracticeprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticeprojectApplication.class, args);
	}

}
