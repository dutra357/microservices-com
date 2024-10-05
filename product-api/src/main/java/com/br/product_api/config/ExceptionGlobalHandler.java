package com.br.product_api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException validationException) {
        var newDetails = new ExceptionDetails();
        newDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        newDetails.setMessage(validationException.getMessage());

        return new ResponseEntity<>(newDetails, HttpStatus.BAD_REQUEST);
    }
}
