package com.yaromich.wallet.domain.dto;

import java.math.BigDecimal;

public class TransactionRequest {
    private String username;
    private String transactionId;
    private BigDecimal amount;
    private String type;

    public TransactionRequest(String username, String transactionId, BigDecimal amount, String type) {
        this.username = username;
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
    }

    public TransactionRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
