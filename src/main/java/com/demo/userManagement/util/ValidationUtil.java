package com.demo.userManagement.util;

public class ValidationUtil {

    private ValidationUtil() {
        // private constructor to prevent instantiation
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }
}
