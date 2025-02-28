package com.example.practiceproject.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SendOtpRequest
{
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number (10 digits required)")
    private String phoneNumber;
}
