package com.maturi.exception.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvisor {

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("잘못된 요청입니다.")
                .build();

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ExceptionHandler(RollbackTriggeredException.class)
    public ResponseEntity<ErrorResponse> rollBackException(RollbackTriggeredException e) throws IOException {
        if (isAjaxRequest(httpServletRequest)) {
            HttpStatus statusCode = e.getStatusCode();

            ErrorResponse body = ErrorResponse.builder()
                    .code(statusCode.value())
                    .message(e.getMessage())
                    .validation(e.getValidation())
                    .build();
            return ResponseEntity.status(statusCode)
                    .body(body);
        } else {
            httpServletResponse.sendError(e.getStatusCode().value());
            return null;
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        String contentType = request.getHeader("Content-Type");
        String requestURI = request.getRequestURI();

        return (accept != null && accept.contains("application/json")) ||
                (contentType != null && contentType.contains("application/json")) ||
                requestURI.startsWith("/api");
    }
}
