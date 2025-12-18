package com.floqdrive.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Centralized error handling
@RestControllerAdvice
public class GlobalExceptionHandler
{
    // 404
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException exception)
    {
        return ResponseEntity.status(404).body(exception.getMessage());
    }

    // 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException exception)
    {
        return ResponseEntity.status(400).body(exception.getMessage());
    }
}
