package com.example.socks_warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataFormatException.class)
    public ResponseEntity<String> handleValidationException(InvalidDataFormatException e) {
        return ResponseEntity.badRequest().body("Invalid data format: " + e.getMessage());
    }

    @ExceptionHandler(InsufficientSocksException.class)
    public ResponseEntity<String> handleInsufficientSocksException(InsufficientSocksException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleFileProcessingException(FileProcessingException e) {
        return ResponseEntity.badRequest().body("File processing error: " + e.getMessage());
    }
    
    @ExceptionHandler(SocksNotFoundException.class)
    public ResponseEntity<String> handleSocksNotFoundException(SocksNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleSocksNotFoundException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>("Invalid input data", HttpStatus.BAD_REQUEST);
    }
}
