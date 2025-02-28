package com.example.practiceproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest
{
    @JsonProperty(value = "To")
    private String to;

    @JsonProperty(value = "Channel")
    private String channel;
}
