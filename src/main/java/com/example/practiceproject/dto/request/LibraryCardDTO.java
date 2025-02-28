package com.example.practiceproject.dto.request;

import com.example.practiceproject.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LibraryCardDTO
{
    private String cardNumber;
    private LocalDate issueDate;
    private LocalTime issueTime;
    private User user;
    private String address;
}
