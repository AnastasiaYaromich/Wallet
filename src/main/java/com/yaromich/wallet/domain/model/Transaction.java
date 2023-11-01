package com.yaromich.wallet.domain.model;

public class Transaction {
    private int id;
    private String transactionId;
    private String type;
    private String condition;
    private String note;
    private User user;

    public Transaction(int id, String transactionId, String type, String condition, String note, User user) {
        this.id = id;
        this.transactionId = transactionId;
        this.type = type;
        this.condition = condition;
        this.note = note;
        this.user = user;
    }

    public Transaction() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
