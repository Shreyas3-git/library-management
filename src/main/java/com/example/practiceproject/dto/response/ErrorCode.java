package com.example.practiceproject.dto.response;

public enum ErrorCode
{
    OK(200),
    BAD_REQUEST(400),
    UNEXPECTED_ERROR(500),
    DEPENDENCY_FAILED(424),
    CONFLICT(409);
    private int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
