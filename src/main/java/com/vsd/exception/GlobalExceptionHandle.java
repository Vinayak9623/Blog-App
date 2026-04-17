package com.vsd.exception;

import com.vsd.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(value ={ResourceNotFoundException.class})
    public ResponseEntity<ApiResponse> handleException(
            ResourceNotFoundException ex){
     return new ResponseEntity(ApiResponse.create(ex.getMessage(),
             HttpStatus.NOT_FOUND.value(),
             HttpStatus.NOT_FOUND.name()), HttpStatus.NOT_FOUND);
    }

}
