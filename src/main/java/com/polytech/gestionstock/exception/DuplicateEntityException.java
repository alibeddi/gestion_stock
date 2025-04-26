package com.polytech.gestionstock.exception;

import lombok.Getter;

@Getter
public class DuplicateEntityException extends RuntimeException {
    
    private final String entityName;
    private final String fieldName;
    private final Object fieldValue;
    
    public DuplicateEntityException(String entityName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s : '%s'", entityName, fieldName, fieldValue));
        this.entityName = entityName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
} 