package com.yaromich.wallet.domain.dto;


public class TransactionDto {
    private int id;
    private String transactionId;
    private String type;
    private String condition;
    private String note;
    private UserDto userDto;

    public TransactionDto(int id, String transactionId, String type, String condition, String note, UserDto userDto) {
        this.id = id;
        this.transactionId = transactionId;
        this.type = type;
        this.condition = condition;
        this.note = note;
        this.userDto = userDto;
    }

    public TransactionDto() {
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

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
