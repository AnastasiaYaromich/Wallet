package ru.yaromich.walletservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
    private int id;
    private String transactionId;
    private String type;
    private String condition;
    private String note;
    private User user;
}
