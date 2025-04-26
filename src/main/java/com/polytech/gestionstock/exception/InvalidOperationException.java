package com.polytech.gestionstock.exception;

import lombok.Getter;

@Getter
public class InvalidOperationException extends RuntimeException {
    
    private final String reason;
    
    public InvalidOperationException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
    
    public InvalidOperationException(String message) {
        super(message);
        this.reason = "Operation not allowed";
    }
} 