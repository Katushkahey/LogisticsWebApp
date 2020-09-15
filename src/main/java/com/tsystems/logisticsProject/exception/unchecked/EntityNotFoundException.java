package com.tsystems.logisticsProject.exception.unchecked;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String parameter, Class entityClass) {
        super((String.format("Entity %s with %s not found", entityClass.getCanonicalName(), parameter)));
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
