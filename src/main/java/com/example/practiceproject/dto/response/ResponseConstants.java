package com.example.practiceproject.dto.response;

public class ResponseConstants
{
    public static final String SUCCESS_MESSAGE = "User registration successful";

    public static final String RESTRICT_COMMON_MESSAGE = "Please Enter Valid Rrn";

    public static final String CONFLICT_MESSAGE = "User Already Exists";

    public static final String FAILED_REGISTRATION_MESSAGE = "User registration Failed";

    public static class NotificationResponse {
        public static final String SUCCESS_SENDOTP_MESSAGE = "OTP sent successfully";

        public static final String FAILED_SENDOTP_MESSAGE = "Failed to send OTP";

        public static final String ERROR_MESSAGE = "Unable to send otp at this movement, Please try after sometime";

        public static final String SUCCESS_VERIFYOTP_MESSAGE = "Otp Verified Successfully";

        public static final String FAILED_VERIFYOTP_MESSAGE  = "Failed to Verify Otp";

        public static final String RESTRICT_VERIFYOTP_MESSAGE = "Please Enter Valid Sid";

        public static final String ERROR_MESSAGE_VERIFYOTP = "Unable to verify otp at this movement, Please try after sometime";
    }

    public static class BookResponse {
        public static String SUCCESS_BOOK_MESSAGE = "Book Created Successfully";

        public static String FAILED_BOOK_MESSAGE = "Book Not Found";

        public static String FAILED_DUETO_LIBRARY_CARD = "Library card not found for user";

        public static String SUCCESS_BOOK_ISSUE = "Book Issued Successfully";
    }

}
