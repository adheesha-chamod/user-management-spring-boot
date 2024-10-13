package com.demo.userManagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnprocessableEntityException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Unprocessable Entity";
    private final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

    public UnprocessableEntityException() {
        super(DEFAULT_MESSAGE);
    }

    public UnprocessableEntityException(String message) {
        super(message);
    }

    public UnprocessableEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnprocessableEntityException(Throwable cause) {
        super(cause);
    }
}
