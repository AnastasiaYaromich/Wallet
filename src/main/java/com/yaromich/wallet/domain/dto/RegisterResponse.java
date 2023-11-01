package com.yaromich.wallet.domain.dto;

public class RegisterResponse {
    private String message;

    public RegisterResponse(String message) {
        this.message = message;
    }

    public RegisterResponse() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
