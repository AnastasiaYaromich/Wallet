package com.yaromich.wallet.domain.dto;

import java.math.BigDecimal;

public class AuditDto {
    private int id;
    private String type;
    private String status;
    private BigDecimal balance;
    private String dateTime;
    private String note;
    private UserDto userDto;

    public AuditDto(int id, String type, String status, BigDecimal balance, String dateTime, String note, UserDto userDto) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.balance = balance;
        this.dateTime = dateTime;
        this.note = note;
        this.userDto = userDto;
    }

    public AuditDto() {}

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

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
