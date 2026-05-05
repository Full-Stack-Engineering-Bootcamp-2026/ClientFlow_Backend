package com.app.exception;

import com.app.constant.ErrorCodes;

public class ResourceNotFoundException extends RuntimeException {

    private final String errorCode;

    public ResourceNotFoundException(String message) {
        super(message);
        this.errorCode = ErrorCodes.NOT_FOUND;
    }

    public String getErrorCode() {
        return errorCode;
    }
}