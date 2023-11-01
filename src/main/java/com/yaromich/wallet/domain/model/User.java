package com.yaromich.wallet.domain.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;


public class User {
    private Long id;
    private String login;
    private String password;
    private Collection<Role> roles;
    List<Transaction> transactions;
    List<Audit> audits;
    private BigDecimal balance;

    public User(Long id, String login, String password, Collection<Role> roles, List<Transaction> transactions, List<Audit> audits, BigDecimal balance) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.transactions = transactions;
        this.audits = audits;
        this.balance = balance;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Audit> getAudits() {
        return audits;
    }

    public void setAudits(List<Audit> audits) {
        this.audits = audits;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
