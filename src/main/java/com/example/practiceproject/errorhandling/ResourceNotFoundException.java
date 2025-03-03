package com.example.practiceproject.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException
{
    private String message;
    private String errorCode;
    private String status;
    private LocalDateTime timestamp;
}
