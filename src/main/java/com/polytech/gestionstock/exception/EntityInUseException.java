package com.polytech.gestionstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityInUseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityInUseException(String entityName, Object entityId, String relatedEntityName, int count) {
        super(String.format("Cannot delete %s with id %s because it is used by %d %s", 
                entityName, entityId, count, relatedEntityName));
    }
} 