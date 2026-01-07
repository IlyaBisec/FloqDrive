package com.floqdrive.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.stream.Collectors;

// Centralized error handling
@RestControllerAdvice
public class GlobalExceptionHandler
{
    /**
     * Validation errors (DTO @Valid)
     * 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception)
    {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .badRequest()
                .body(error("VALIDATION_ERROR", message));
    }

    /**
     * Resource not found
     * 404 Not Found
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException exception)
    {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error("NOT_FOUND", exception.getMessage()));
    }

    /**
     * Illegal arguments from business logic
     * 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException exception)
    {
        return ResponseEntity
                .badRequest()
                .body(error("BAD_REQUEST", exception.getMessage()));
    }

    /**
     * Illegal state (conflict, invalid flow)
     * 409 Conflict
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>>handleIllegalState(IllegalStateException exception)
    {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error("CONFLICT", exception.getMessage()));
    }

    /**
     * Database errors
     * 500 Internal Server Error
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataBase(DataAccessException exception)
    {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("DATABASE_ERROR", "Database error occurred"));
    }

    /**
     * Fallback handler
     * 500 Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAny(Exception exception)
    {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("INTERNAL_SERVER_ERROR", "Unexpected server error"));
    }

    /**
     * Unified error response
     */
    private Map<String, Object> error(String code, String message)
    {
        return Map.of(
                "error", code,
                "message", message
        );
    }

}
