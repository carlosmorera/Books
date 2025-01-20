package com.wearedev.demo.infrastructure.adapter.in.web;

import com.wearedev.demo.application.exception.BookNotFoundException;
import com.wearedev.demo.application.exception.InvalidBookException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBookNotFoundException(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
            "error", "Book Not Found",
            "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<Map<String, String>> handleInvalidBookException(InvalidBookException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "error", "Invalid Book Data",
            "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "error", "Unexpected Error",
            "message", ex.getMessage()
        ));
    }
}