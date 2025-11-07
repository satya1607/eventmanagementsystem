package com.example.eventmanagementsystem.exception;

public class DataAlreadyExistsException extends RuntimeException {
    public DataAlreadyExistsException(String message) {
        super(message);
    }
}
