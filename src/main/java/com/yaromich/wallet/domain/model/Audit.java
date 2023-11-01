package com.yaromich.wallet.domain.model;

import java.math.BigDecimal;

public class Audit {
    private int id;
    private String type;
    private String status;
    private BigDecimal balance;
    private String dateTime;
    private String note;
    private User user;

    public Audit(int id, String type, String status, BigDecimal balance, String dateTime, String note, User user) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.balance = balance;
        this.dateTime = dateTime;
        this.note = note;
        this.user = user;
    }

    public Audit() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
