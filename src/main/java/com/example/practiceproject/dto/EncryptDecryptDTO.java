package com.example.practiceproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EncryptDecryptDTO
{
    private String encryptedData;
    private String encryptedKey;
}
