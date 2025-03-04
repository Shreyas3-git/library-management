package com.example.practiceproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookRequest
{
    @NotNull(message = "Title should not be null")
    @NotBlank(message = "Title Should not be blank")
    private String title;
    @NotNull(message = "Author should not be null")
    @NotBlank(message = "author Should not be blank")
    private String author;

    private Integer quantity;

    @NotNull(message = "RRN should not be null")
    @NotBlank(message = "RRN Should not be blank")
    private String rrn;
}
