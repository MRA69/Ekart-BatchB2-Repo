package com.ekart.batchB2.exceptionhandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(e.getMessage());
        errorMessage.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(e.getMessage());
        errorMessage.setStatusCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", ")));
        errorMessage.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", ")));
        errorMessage.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}