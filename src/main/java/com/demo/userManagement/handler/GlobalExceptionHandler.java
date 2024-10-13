package com.demo.userManagement.handler;

import com.demo.userManagement.exception.NotFoundException;
import com.demo.userManagement.exception.UnprocessableEntityException;
import com.demo.userManagement.handler.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(getErrorResponse(ex.getMessage(), ex.getCause()));
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorResponse> handleUnprocessableEntityException(UnprocessableEntityException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(getErrorResponse(ex.getMessage(), ex.getCause()));
    }

    // general exception handler for all runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error(String.format("Unexpected error occurred: %s", ex.getMessage()), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(getErrorResponse("Internal server error", ex.getCause()));
    }

    private ErrorResponse getErrorResponse(String message, Throwable cause) {
        return new ErrorResponse(
                message,
                LocalDateTime.now(),
                cause == null ? "N/A" : cause.getMessage()
        );
    }
}
