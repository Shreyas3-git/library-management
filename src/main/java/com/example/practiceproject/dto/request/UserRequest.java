package com.example.practiceproject.dto.request;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest
{
    private String name;
    private String email;
    private String contact;
    private String address;
    private int pinCode;
    private String password;
    private LibraryCardDTO libraryCard;
    private String rrn;
}
