package com.app.exception;

import com.app.constant.ErrorCodes;

public class UnauthorizedException extends RuntimeException {

    private final String errorCode;

    public UnauthorizedException(String message) {
        super(message);
        this.errorCode = ErrorCodes.UNAUTHORIZED;
    }

    public String getErrorCode() {
        return errorCode;
    }
}