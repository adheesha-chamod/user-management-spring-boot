package com.demo.userManagement.user.exception;

import com.demo.userManagement.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String userId) {
        super(String.format("User with id %s not found", userId));
    }

    public UserNotFoundException(String userId, Throwable cause) {
        super(
                String.format("User with id %s not found", userId),
                cause
        );
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
