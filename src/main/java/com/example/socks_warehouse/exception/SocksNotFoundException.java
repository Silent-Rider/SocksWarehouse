package com.example.socks_warehouse.exception;

public class SocksNotFoundException extends RuntimeException{
    public SocksNotFoundException(String message) {
        super(message);
    }
}
