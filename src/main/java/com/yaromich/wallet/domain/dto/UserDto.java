package com.yaromich.wallet.domain.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class UserDto {
    private long id;
    private String login;
    private String password;
    private Collection<RoleDto> roles;
    private List<TransactionDto> transactions;
    private List<AuditDto> audits;
    private BigDecimal balance;

    public UserDto(long id, String login, String password, Collection<RoleDto> roles, List<TransactionDto> transactions, List<AuditDto> audits, BigDecimal balance) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.transactions = transactions;
        this.audits = audits;
        this.balance = balance;
    }

    public UserDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Collection<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleDto> roles) {
        this.roles = roles;
    }

    public List<TransactionDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDto> transactions) {
        this.transactions = transactions;
    }

    public List<AuditDto> getAudits() {
        return audits;
    }

    public void setAudits(List<AuditDto> audits) {
        this.audits = audits;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
