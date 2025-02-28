package com.example.practiceproject.model;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpResponse
{
    private String status;
    private String message;
    private String verificationSid;
}
