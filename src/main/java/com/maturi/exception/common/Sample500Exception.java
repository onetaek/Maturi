package com.maturi.exception.common;

import org.springframework.http.HttpStatus;

public class Sample500Exception extends RollbackTriggeredException{
    public Sample500Exception() {
        super("샘플 500예외");
    }

    @Override
    public HttpStatus getStatusCode() {
        return null;
    }
}
