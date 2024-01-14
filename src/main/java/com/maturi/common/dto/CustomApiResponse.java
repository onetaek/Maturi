package com.maturi.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CustomApiResponse<T> extends ResponseEntity<T> {

    private String message;

    public CustomApiResponse(HttpStatus httpStatus, T data) {
        super(data, httpStatus);
    }

    public CustomApiResponse(HttpStatus httpStatus, T data, String message) {
        super(data, httpStatus);
        this.message = message;
    }

    public static <T> CustomApiResponse<T> of(HttpStatus httpStatus, T data) {
        return new CustomApiResponse<>(httpStatus, data);
    }

    public static <T> CustomApiResponse<T> of(HttpStatus httpStatus, T data, String message) {
        return new CustomApiResponse<>(httpStatus, data, message);
    }

    public static <T> CustomApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, data, HttpStatus.OK.name());
    }

    public static <T> CustomApiResponse<T> created(T data) {
        return of(HttpStatus.CREATED, data, HttpStatus.CREATED.name());
    }

    public static <T> CustomApiResponse<T> noContent(T data) {
        return of(HttpStatus.NO_CONTENT, data, HttpStatus.NO_CONTENT.name());
    }

    public static <T> CustomApiResponse<T> error(HttpStatus httpStatus,T data) {
        return of(httpStatus, data, httpStatus.name());
    }
}
