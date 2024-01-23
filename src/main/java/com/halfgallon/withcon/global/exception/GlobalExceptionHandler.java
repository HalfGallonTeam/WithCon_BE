package com.halfgallon.withcon.global.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getStatusCode(), e.getMessage());
    return ResponseEntity.status(errorResponse.status()).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getStatusCode().value(), e.getMessage());
    return ResponseEntity.status(errorResponse.status()).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage());

    return ResponseEntity.status(errorResponse.status()).body(errorResponse);
  }
}

