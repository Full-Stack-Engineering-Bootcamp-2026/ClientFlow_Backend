package com.app.exception;

import com.app.constant.ErrorCodes;

public class DuplicateResourceException extends RuntimeException {

    private final String errorCode;

    public DuplicateResourceException(String message) {
        super(message);
        this.errorCode = ErrorCodes.DUPLICATE;
    }

    public String getErrorCode() {
        return errorCode;
    }
}