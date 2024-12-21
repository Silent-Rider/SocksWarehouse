package com.example.socks_warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataFormatException.class)
    public ResponseEntity<String> handleInvalidDataFormatException(InvalidDataFormatException e, 
    HttpServletRequest request) {
        String message = "Invalid data format: " + e.getMessage();
        log.error("{} {}: {}", request.getMethod(), request.getRequestURI(), message, e);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(InsufficientSocksException.class)
    public ResponseEntity<String> handleInsufficientSocks(InsufficientSocksException e, HttpServletRequest request) {
        log.error("{} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleFileProcessingException(FileProcessingException e, HttpServletRequest request) {
        log.error("{} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(SocksNotFoundException.class)
    public ResponseEntity<String> handleSocksNotFound(SocksNotFoundException e, HttpServletRequest request) {
        log.error("{} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMessageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        String message = "Invalid input data";
        log.error("{} {}: {}", request.getMethod(), request.getRequestURI(), message, e);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParam(MissingServletRequestParameterException e, HttpServletRequest request){
        String message = "Missed obligatory parameter";
        log.error("{} {}: {}", request.getMethod(), request.getRequestURI(), message, e);
        return ResponseEntity.badRequest().body(message);
    }
}
