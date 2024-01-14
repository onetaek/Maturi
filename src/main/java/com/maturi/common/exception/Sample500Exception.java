package com.maturi.common.exception;

import org.springframework.http.HttpStatus;

public class Sample500Exception extends RollbackTriggeredException{
    public Sample500Exception() {
        super("샘플 500예외");
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
