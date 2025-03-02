package com.example.practiceproject;

import com.example.practiceproject.utils.loader.EncryptionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.OAuth2ResourceServerSecurityMarker;

@SpringBootApplication
@EnableConfigurationProperties(EncryptionConfig.class)
@EnableFeignClients
@OAuth2ResourceServerSecurityMarker
//@SecurityMarker
public class PracticeprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticeprojectApplication.class, args);
	}

}
