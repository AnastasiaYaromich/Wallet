package ru.yaromich.walletservice.logic.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> handleUserException(UserNotFoundException e) {
        return new ResponseEntity<AppError>(new AppError("USER_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleUserException(UserAlreadyExistException e) {
        return new ResponseEntity<AppError>(new AppError("USER_ALREADY_EXIST", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleUserException(BadCredentialsException e) {
        return new ResponseEntity<AppError>(new AppError("INVALID_LOGIN_OR_PASSWORD", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleTransactionException(NotEnoughMoneyException e) {
        return new ResponseEntity<AppError>(new AppError("NOT_ENOUGH_MONEY", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleTransactionException(TransactionIdIsNotUniqueException e) {
        return new ResponseEntity<AppError>(new AppError("TRANSACTION_ID_IS_NOT_UNIQUE", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
