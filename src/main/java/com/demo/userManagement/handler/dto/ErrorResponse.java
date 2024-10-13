package com.demo.userManagement.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private String message;
    private LocalDateTime timestamp;
    private String cause;
}
