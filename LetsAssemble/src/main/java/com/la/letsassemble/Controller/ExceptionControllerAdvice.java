package com.la.letsassemble.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResult> testing(NullPointerException e){
        ErrorResult errorResult=new ErrorResult("EMAIL",e.getMessage());
        return new ResponseEntity<>(errorResult,HttpStatus.BAD_REQUEST);
    }
}