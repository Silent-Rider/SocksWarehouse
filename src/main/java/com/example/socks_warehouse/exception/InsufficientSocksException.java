package com.example.socks_warehouse.exception;

public class InsufficientSocksException extends RuntimeException {
    public InsufficientSocksException(String message) {
        super(message);
    }
}
