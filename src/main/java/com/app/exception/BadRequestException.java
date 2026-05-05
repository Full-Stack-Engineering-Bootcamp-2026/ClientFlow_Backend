package com.app.exception;

import com.app.constant.ErrorCodes;

public class BadRequestException extends RuntimeException {

    private final String errorCode;

    public BadRequestException(String message) {
        super(message);
        this.errorCode = ErrorCodes.BAD_REQUEST;
    }

    public String getErrorCode() {
        return errorCode;
    }
}