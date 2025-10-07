package com.brahim.book_network_api.exception;

public class OperationNotPermittedException extends RuntimeException {
    
    public OperationNotPermittedException(String message) {
        super(message);
    }
}
