package com.example.practiceproject.model;

import lombok.Data;

@Data
public class SendCodeAttemptsItem{
	private String attemptSid;
	private String channel;
	private String time;
}