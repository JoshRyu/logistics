package com.madeg.logistics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.madeg.logistics.domain.ResponseCommon;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseCommon> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "INVALID JSON FORMAT";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseCommon(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }
}
