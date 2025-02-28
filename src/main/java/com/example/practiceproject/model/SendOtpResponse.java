package com.example.practiceproject.model;

import java.util.List;
import lombok.Data;

@Data
public class SendOtpResponse {
	private Lookup lookup;
	private Object amount;
	private String dateUpdated;
	private String dateCreated;
	private String channel;
	private String url;
	private String sid;
	private Object payee;
	private boolean valid;
	private String serviceSid;
	private String accountSid;
	private String to;
	private String status;
	private List<SendCodeAttemptsItem> sendCodeAttempts;
}