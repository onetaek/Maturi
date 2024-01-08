package com.maturi.exception.common;

import org.springframework.http.HttpStatus;

public class Sample404Exception extends RollbackTriggeredException {

    public Sample404Exception() {
        super("샘플 404 예외");
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
