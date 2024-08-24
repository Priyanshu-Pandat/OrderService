package com.dailycodebuffer.orderservice.exception;

import com.dailycodebuffer.orderservice.external.responce.ErrorResponce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponceEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponce> handleCustomException(CustomException exception) {
        return new  ResponseEntity<>(new ErrorResponce().builder()
                .errorMessage(exception.getMessage())
                 .errorCode(exception.getErrorCode())
                 .build(), HttpStatus.valueOf(
                         exception.getStatus()));
    }
}
