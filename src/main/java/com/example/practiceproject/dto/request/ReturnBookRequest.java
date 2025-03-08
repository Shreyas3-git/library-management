package com.example.practiceproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReturnBookRequest
{
    @NotNull(message = "Title should not be null")
    @NotBlank(message = "Title should not be blank")
    private String title;
    @NotNull(message = "author should not be null")
    @NotBlank(message = "author should not be blank")
    private String author;
    @NotNull(message = "userId should not be null")
    private Long userId;
    @NotNull(message = "rrn should not be null")
    @NotBlank(message = "rrn should not be blank")
    private String rrn;

}
