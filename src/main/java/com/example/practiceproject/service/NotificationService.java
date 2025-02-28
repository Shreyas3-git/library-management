package com.example.practiceproject.service;

import com.example.practiceproject.dto.request.SendOtpRequest;
import com.example.practiceproject.dto.request.VerifyOtpRequest;
import com.example.practiceproject.dto.response.CommonResponse;
import com.example.practiceproject.entity.Attempts;
import com.example.practiceproject.entity.Notifications;
import com.example.practiceproject.entity.User;
import com.example.practiceproject.feignconfig.NotificationClient;
import com.example.practiceproject.model.SendOtpResponse;
import com.example.practiceproject.model.VerifyOtpResponse;
import com.example.practiceproject.repository.AttemptsRepository;
import com.example.practiceproject.repository.NotificationsRepository;
import com.example.practiceproject.repository.UserRepository;
import com.example.practiceproject.utils.DatabaseEncryptionUtils;
import com.google.gson.Gson;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

import static com.example.practiceproject.utils.DatabaseEncryptionUtils.encrypt;

@Service
@Transactional
public class NotificationService
{
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Value("${app.notifications.twilio.account-sid}")
    private String accountSid;
    @Value("${app.notifications.twilio.service-sid}")
    private String serviceSid;
    @Value("${app.notifications.twilio.auth-token}")
    private String authToken;
    @Autowired
    private NotificationsRepository notificationsRepository;
    @Autowired
    private NotificationClient notificationClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttemptsRepository attemptsRepository;


    @PostConstruct
    public void logTwilioConfig() {
        System.out.println("Twilio Account SID: " + accountSid);
        System.out.println("Twilio Service SID: " + serviceSid);
        System.out.println("Twilio Auth Token: " + authToken);
    }
    public ResponseEntity<CommonResponse> sendOtp(SendOtpRequest request) {
        try {
            String phoneNum = formatToE164(request.getPhoneNumber());
            byte[] phoneNumber = encrypt(phoneNum);
            log.info(String.format("SendOtpRequest : %s",new Gson().toJson(request)));
            String auth = getBasicAuthHeader.get();
            Map<String,String> hMap = new HashMap<>();
            hMap.put("Content-Type","application/x-www-form-urlencoded");
            hMap.put("Authorization",auth);
            String encodedTo = URLEncoder.encode(phoneNum,  StandardCharsets.UTF_8);
            String requestBody = "To=" + encodedTo + "&Channel=sms";
            log.info(String.format("OtpRequest : %s and Request Header : %s", new Gson().toJson(requestBody), hMap));
            ResponseEntity<SendOtpResponse> otpRes = notificationClient.sendOtp(serviceSid,hMap,requestBody);
            log.info( String.format("OtpResponse %s",new Gson().toJson(otpRes.getBody()) ));
            String rrn = String.valueOf(UUID.randomUUID()) + "-" + System.currentTimeMillis();
            return Optional.of(otpRes).filter(res -> res.getStatusCode().value() == 201)
                .map(otpResp -> {
                    SendOtpResponse otpResponse = otpResp.getBody();
                    User user = User.builder().rrn(rrn).contact(phoneNumber).build();
                    userRepository.save(user);
                    assert otpResponse != null;
                    Notifications notifications = Notifications.builder()
                        .referenceNumber(otpResponse.getSid())
                        .phoneNumber(phoneNumber)
                        .status(otpResponse.getStatus())
                        .createdAt(LocalDateTime.now())
                        .user(user).build();

                    Attempts attempts = Attempts.builder().attemptsSid(otpResponse.getSid())
                        .notifications(notifications)
                        .channel(otpResponse.getChannel())
                        .sentTime(LocalDateTime.now()).build();

                    notifications.setSendCodeAttempts(Collections.singletonList(attempts)); // ***CRITICAL: Set the attempts for the notification***
                    notificationsRepository.save(notifications);

                    CommonResponse resp = CommonResponse.builder().message("OTP sent successfully")
                            .status("SUCCESS")
                            .rrn(rrn)
                            .timestamp(LocalDateTime.now())
                            .errorCode("200").build();
                    return new ResponseEntity<>(resp, HttpStatus.OK);
                }).orElseGet(() -> {
                    CommonResponse resp = CommonResponse.builder().message("Failed to send OTP")
                            .status("FAILED")
                            .timestamp(LocalDateTime.now())
                            .errorCode("400").build();
                    return new ResponseEntity<>(resp, HttpStatus.FAILED_DEPENDENCY);
                });
        } catch ( FeignException ex ) {
            log.error(String.format("FeignException in Twilio SendOtp : %s",ex));
            CommonResponse resp = CommonResponse.builder().message("Unable verify otp at this movement, Please try after sometime")
                    .status("FAILED")
                    .timestamp(LocalDateTime.now())
                    .errorCode("400").build();
            return new ResponseEntity<>(resp, HttpStatus.FAILED_DEPENDENCY);
        } catch ( IllegalArgumentException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException e) {
            log.error(String.format("Unable to verify otp at this movement : %s",e));
            CommonResponse resp = CommonResponse.builder().message("Unable send otp at this movement, Please try after sometime")
                    .status("FAILED")
                    .timestamp(LocalDateTime.now())
                    .errorCode("500").build();
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    private final Supplier<String> getBasicAuthHeader = () -> {
        String credentials = accountSid + ":" + authToken;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    };


    private String formatToE164(String phoneNumber) {
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");
        return "+91" + digitsOnly;
    }


    public ResponseEntity<CommonResponse> verifyOtp(VerifyOtpRequest request) {
        try {
            return userRepository.findByRrn(request.getRrn()).map(user -> {
                return notificationsRepository.findByReferenceNumber(request.getSid())
                    .map(notify -> {
                        String phoneNum = DatabaseEncryptionUtils.decrypt(user.getContact());
                        Map<String,String> hMap = new HashMap<>();
                        hMap.put("Content-Type","application/x-www-form-urlencoded");
                        hMap.put("Authorization",getBasicAuthHeader.get());
                        assert phoneNum != null;
                        String encodedPhoneNum = URLEncoder.encode(phoneNum,StandardCharsets.UTF_8);
                        String encodedOtp = URLEncoder.encode(request.getOtp(),StandardCharsets.UTF_8);
                        String requestBody = "Code=" + encodedOtp + "&To=" + encodedPhoneNum;
                        log.info(String.format("VerifyOtp Request : %s and Request Header : %s",requestBody,hMap));
                        String verifyOtpResp = notificationClient.verifyOtp(serviceSid,hMap,requestBody);
                        log.info(String.format("VerifyOtp Response : %s => ",verifyOtpResp));
                        VerifyOtpResponse verifyOtpResponse = new Gson().fromJson(verifyOtpResp,VerifyOtpResponse.class);
                        CommonResponse resp = CommonResponse.builder().message("Otp Verified Successfully")
                            .status("SUCCESS")
                            .timestamp(LocalDateTime.now())
                            .errorCode("200").build();
                        return new ResponseEntity<>(resp, HttpStatus.OK);
                    }).orElseGet(() -> {
                        CommonResponse resp = CommonResponse.builder().message("Please Enter Valid Sid")
                            .status("FAILED")
                            .timestamp(LocalDateTime.now())
                            .errorCode("400").build();
                        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
                    });
            }).orElseGet(() -> {
                CommonResponse resp = CommonResponse.builder().message("Please Enter Valid Rrn")
                    .status("FAILED")
                    .timestamp(LocalDateTime.now())
                    .errorCode("400").build();
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            });
        } catch ( FeignException ex ) {
            log.error(String.format("FeignException in Twilio SendOtp : %s",ex));
            CommonResponse resp = CommonResponse.builder().message("Unable send otp at this movement, Please try after sometime")
                .status("FAILED")
                .timestamp(LocalDateTime.now())
                .errorCode("400").build();
            return new ResponseEntity<>(resp, HttpStatus.FAILED_DEPENDENCY);
        } catch ( RuntimeException e) {
            log.error(String.format("Unable send otp at this movement : %s",e));
            CommonResponse resp = CommonResponse.builder().message("Unable send otp at this movement, Please try after sometime")
                .status("FAILED")
                .timestamp(LocalDateTime.now())
                .errorCode("500").build();
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
