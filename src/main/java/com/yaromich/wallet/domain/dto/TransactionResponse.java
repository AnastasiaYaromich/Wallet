package com.yaromich.wallet.domain.dto;

import org.springframework.http.HttpStatus;

public class TransactionResponse {
    private String message;
    private HttpStatus status;

    public TransactionResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public TransactionResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
