package com.lucas.mastermind.controller;

import com.lucas.mastermind.exception.DuplicateNickException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(DuplicateNickException.class)
    public ResponseEntity<?> handleDuplicateNickException(DuplicateNickException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }
}
