package com.example.practiceproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.issuer-uri}")
    private String issuerUri;

    private final Logger log = LoggerFactory.getLogger(SecurityConfig.class);


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(oauth2 -> oauth2
                .requestMatchers("/app/librarymanagement/**").authenticated()
                .anyRequest().permitAll())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()))
                .authenticationEntryPoint(this::handleAuthenticationFailure))
            .exceptionHandling(exception -> exception.accessDeniedHandler(this::handleAccessDenied));
        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret.trim());
        log.info("DECODED BYTES {} =>", Arrays.toString(keyBytes));
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA512");

        // Configure JWT decoder with issuer validation
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key)
            .macAlgorithm(MacAlgorithm.HS512)
            .build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuerUri));
        return decoder;
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response
            , HttpStatus status, String message) throws IOException {

        response.setStatus(status.value());
        response.setContentType("application/json");

        Map<String, String> errorDetails = new LinkedHashMap<>();
        String token =   request.getHeader("Authorization");
        if(token != null ) {
            String bearerToken =  token.substring(7);
            errorDetails.put("bearer_token",bearerToken);
        }
        errorDetails.put("message", message);
        errorDetails.put("status", status.name());
        errorDetails.put("errorCode", String.valueOf(status.value()));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorDetails);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    private void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
             AuthenticationException message) throws IOException {
        sendErrorResponse(request,response,HttpStatus.UNAUTHORIZED,"UNAUTHORIZED: You have not authorize to access this resource");
    }

    private void handleAccessDenied(HttpServletRequest request, HttpServletResponse response,
             AccessDeniedException message) throws IOException {
        sendErrorResponse(request,response,HttpStatus.FORBIDDEN, "ACCESS DENIED: You do not have permission to access this resource");
    }
}
