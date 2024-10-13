package com.demo.userManagement.user.exception;

import com.demo.userManagement.exception.UnprocessableEntityException;

public class InvalidAddUserRequestException extends UnprocessableEntityException {

    public InvalidAddUserRequestException() {
        super("Invalid add user request");
    }

    public InvalidAddUserRequestException(String message) {
        super(message);
    }

    public InvalidAddUserRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAddUserRequestException(Throwable cause) {
        super(cause);
    }
}
