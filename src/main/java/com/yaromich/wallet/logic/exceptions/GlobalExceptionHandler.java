package com.yaromich.wallet.logic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new AppError("RESOURCE_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleUserException(UserNotFoundException e) {
        return new ResponseEntity<>(new AppError("USER_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleUserException(UserAlreadyExistException e) {
        return new ResponseEntity<>(new AppError("USER_ALREADY_EXIST", e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleUserException(NotEnoughMoneyException e) {
        return new ResponseEntity<>(new AppError("NOT_ENOUGH_MONEY", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleUserException(TransactionIdIsNotUniqueException e) {
        return new ResponseEntity<>(new AppError("TRANSACTION_ID_IS_NOT_UNIQUE", e.getMessage()), HttpStatus.CONFLICT);
    }
}
