package com.yaromich.wallet.domain.dto;

public class JwtRequest {
    private String login;
    private String password;

    public JwtRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public JwtRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
