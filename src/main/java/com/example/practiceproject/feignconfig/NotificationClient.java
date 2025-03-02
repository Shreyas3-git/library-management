package com.example.practiceproject.feignconfig;

import com.example.practiceproject.model.OtpResponse;
import com.example.practiceproject.model.OtpRequest;
import com.example.practiceproject.model.SendOtpResponse;
import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "Notification-Service", url = "${app.notifications.baseUrl}", configuration = FeignClientsConfiguration.class)
public interface NotificationClient {

    @PostMapping("${app.notifications.sendOtp.url}")
    public ResponseEntity<SendOtpResponse> sendOtp(@RequestParam("serviceSid") String serviceSid, @RequestHeader Map<String,String> map, @RequestBody String request);

    @PostMapping("${app.notifications.verifyOtp.url}")
    public ResponseEntity<String> verifyOtp(@RequestParam("serviceSid")  String serviceSid, @RequestHeader Map<String,String> hMap, @RequestBody String request);
}
