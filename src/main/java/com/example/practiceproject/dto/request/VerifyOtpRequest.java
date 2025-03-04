package com.example.practiceproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyOtpRequest
{
    @Pattern(regexp="^[0-9]{6}$",message = "Enter correct 6 digit OTP")
    @NotNull(message = "OTP Should not br null")
    private String otp;
    @NotNull(message = "sid Should not br null")
    @NotBlank(message = "sid Should not br blank")
    private String sid;
    @NotNull(message = "rrn Should not br null")
    @NotBlank(message = "rrn Should not br blank")
    private String rrn;
}
