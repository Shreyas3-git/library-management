package com.example.practiceproject.controller;

import com.example.practiceproject.dto.request.SendOtpRequest;
import com.example.practiceproject.dto.request.UserRequest;
import com.example.practiceproject.dto.request.VerifyOtpRequest;
import com.example.practiceproject.dto.response.CommonResponse;
import com.example.practiceproject.model.OtpRequest;
import com.example.practiceproject.service.NotificationService;
import com.example.practiceproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "${app.base-url}")
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("${app.user.create-endpoint}")
    public ResponseEntity<CommonResponse> createUser(@Valid @RequestBody UserRequest request) throws Exception {
        return userService.createUser(request);
    }

    @PostMapping("${app.user.sendOtp}")
    public ResponseEntity<CommonResponse> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        return notificationService.sendOtp(request);
    }

    @PostMapping("${app.user.verifyOtp}")
    public ResponseEntity<CommonResponse> verifyOtp( @RequestBody VerifyOtpRequest request) {
        return notificationService.verifyOtp(request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<CommonResponse> methofArgumentNotValid(MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append(fieldName).append(":").append(message).append(";");
        });
        CommonResponse response = CommonResponse.builder()
                .message(errorMessage.toString())
                .errorCode("400")
                .status("FAILRF")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
