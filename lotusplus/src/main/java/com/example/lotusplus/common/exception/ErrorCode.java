package com.example.lotusplus.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(
            "USER_001",
            "User not found",
            HttpStatus.NOT_FOUND
    ),

    CHECKIN_NOT_AVAILABLE(
            "CHECKIN_001",
            "Check-in is not available at this time",
            HttpStatus.BAD_REQUEST
    ),

    ALREADY_CHECKED_IN(
            "CHECKIN_002",
            "User already checked in today",
            HttpStatus.CONFLICT
    ),

    INVALID_POINT(
            "POINT_001",
            "Invalid point",
            HttpStatus.BAD_REQUEST
    ),

    INSUFFICIENT_POINT(
            "POINT_002",
            "Insufficient Lotus+ points",
            HttpStatus.BAD_REQUEST
    ),

    CONCURRENT_POINT_UPDATE(
            "POINT_003",
            "Point balance was modified by another request. Please try again.",
            HttpStatus.CONFLICT
    ),

    INTERNAL_ERROR(
            "SYSTEM_001",
            "Internal server error",
            HttpStatus.INTERNAL_SERVER_ERROR
    );


    private final String code;

    private final String message;

    private final HttpStatus status;


    ErrorCode(
            String code,
            String message,
            HttpStatus status
    ) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}