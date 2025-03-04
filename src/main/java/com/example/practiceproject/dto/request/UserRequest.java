package com.example.practiceproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Email;

import java.util.UUID;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest
{
    @NotNull(message = "Name should not be null")
    @NotBlank(message = "Name should not be blank")
    private String name;

    @NotNull(message = "email should not be null")
    @Email(message = "Please enter valid email")
    private String email;

    @NotNull(message = "contact should not be null")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid contact number (10 digits required)")
    private String contact;

    @NotNull(message = "address should not be null")
    @NotBlank(message = "address should not be blank")
    private String address;

    @Pattern(regexp = "^[0-9]{6}$",message = "Invalid pinCode")
    private String pinCode;

    @NotNull(message = "password should not be null")
    @NotBlank(message = "password should not be blank")
    private String password;

//    @NotNull(message = "libraryCard details should not be null")
//    @NotBlank(message = "libraryCard details should not be blank")
    private LibraryCardDTO libraryCard;

    @NotNull(message = "rrn should not be null")
    @NotBlank(message = "rrn should not be blank")
    private String rrn;
}
