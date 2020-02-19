package com.nittsu.kinjirou.identity.common;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Error model for interacting with client.
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    // Time stamp
    private final Date timestamp = new Date();

    // General Error message
    @NonNull
    private final String message;

    // Error code
    @NonNull
    private final ErrorCode errorCode;

    // HTTP Response Status Code
    @NonNull
    private final HttpStatus status;

    public static ErrorResponse of(final String message, final ErrorCode errorCode, final HttpStatus status) {
        return new ErrorResponse(message, errorCode, status);
    }
}