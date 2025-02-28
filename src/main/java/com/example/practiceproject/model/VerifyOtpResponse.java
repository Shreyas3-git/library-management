package com.example.practiceproject.model;

import lombok.Data;

@Data
public class VerifyOtpResponse{
	private Object payee;
	private boolean valid;
	private String serviceSid;
	private Object amount;
	private String dateUpdated;
	private String dateCreated;
	private String channel;
	private String accountSid;
	private String to;
	private String status;
	private String sid;
}