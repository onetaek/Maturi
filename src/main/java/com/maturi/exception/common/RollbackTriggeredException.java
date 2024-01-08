package com.maturi.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class RollbackTriggeredException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public RollbackTriggeredException(String message) {
        super(message);
    }

    public RollbackTriggeredException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract HttpStatus getStatusCode();

    public void addValidation(String filedName, String message) {
        validation.put(filedName, message);
    }
}
