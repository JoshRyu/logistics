package com.madeg.logistics.exception;

import com.madeg.logistics.domain.CommonRes;
import org.springframework.http.HttpStatus;
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
    HttpMessageNotReadableException ex
  ) {
    String errorMessage = ex.getLocalizedMessage();
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(new CommonRes(HttpStatus.BAD_REQUEST.value(), errorMessage));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(
    MethodArgumentNotValidException ex
  ) {
    FieldError fieldError = ex.getBindingResult().getFieldError();
    String errorMessage = (fieldError != null)
      ? fieldError.getDefaultMessage()
      : "Bad Request";

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(new CommonRes(HttpStatus.BAD_REQUEST.value(), errorMessage));
  }
}
