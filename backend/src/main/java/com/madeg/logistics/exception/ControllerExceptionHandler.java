package com.madeg.logistics.exception;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.enums.ResponseCode;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<CommonRes> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    String errorMessage = ex.getLocalizedMessage();
    return ResponseEntity
        .status(ResponseCode.BAD_REQUEST.getStatus())
        .body(new CommonRes(ResponseCode.BAD_REQUEST.getCode(), errorMessage));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    FieldError fieldError = ex.getBindingResult().getFieldError();
    String errorMessage = (fieldError != null)
        ? fieldError.getDefaultMessage()
        : "잘못된 요청입니다";

    return ResponseEntity
        .status(ResponseCode.BAD_REQUEST.getStatus())
        .body(new CommonRes(ResponseCode.BAD_REQUEST.getCode(), errorMessage));
  }
}
