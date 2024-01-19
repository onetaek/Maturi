package com.maturi.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CustomApiResponse<T> extends ResponseEntity<T> {

    public CustomApiResponse(HttpStatus httpStatus, T data) {
        super(data, httpStatus);
    }

    public static <T> CustomApiResponse<T> of(HttpStatus httpStatus, T data) {
        return new CustomApiResponse<>(httpStatus, data);
    }

    public static <T> CustomApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, data);
    }

    public static <T> CustomApiResponse<T> created(T data) {
        return of(HttpStatus.CREATED, data);
    }

    public static <T> CustomApiResponse<T> noContent(T data) {
        return of(HttpStatus.NO_CONTENT, data);
    }

    public static <T> CustomApiResponse<T> error(HttpStatus httpStatus,T data) {
        return of(httpStatus, data);
    }
}
