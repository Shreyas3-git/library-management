package com.example.practiceproject.controller;

import com.example.practiceproject.dto.request.*;
import com.example.practiceproject.dto.response.CommonResponse;
import com.example.practiceproject.model.OtpRequest;
import com.example.practiceproject.service.BookService;
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

    @Autowired
    private BookService bookService;

    @PostMapping("${app.user.create-user}")
    public ResponseEntity<CommonResponse> createUser(@Valid @RequestBody UserRequest request) throws Exception {
        return userService.createUser(request);
    }

    @PostMapping("${app.user.sendOtp}")
    public ResponseEntity<CommonResponse> sendOtp(@RequestHeader("Authorization") String token, @Valid @RequestBody SendOtpRequest request) {
        return notificationService.sendOtp(request);
    }

    @PostMapping("${app.user.verifyOtp}")
    public ResponseEntity<CommonResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return notificationService.verifyOtp(request);
    }

    @PostMapping("${app.user.create-book}")
    public ResponseEntity<CommonResponse> createBook(@Valid @RequestBody CreateBookRequest request) {
        return bookService.createBook(request);
    }

    @PostMapping("${app.user.issue-book}")
    public ResponseEntity<CommonResponse> issueBook(@Valid @RequestBody IssueBookRequest request) {
        return bookService.issueBook(request);
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
